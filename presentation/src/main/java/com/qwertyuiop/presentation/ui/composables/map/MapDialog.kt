package com.qwertyuiop.presentation.ui.composables.map

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.qwertyuiop.presentation.ui.composables.map.mvi.DialogType
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.CloseFirstDialog
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.GetDirectionClicked
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.NewMarkerAdded
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.RemoveMarkerClicked
import com.test_task.presentation.R
import org.koin.compose.koinInject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun MapDialog(
    dialogType: DialogType,
    onEvent: (MapEvent) -> Unit
) = Dialog(
    onDismissRequest = { onEvent(CloseFirstDialog) }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (dialogType) {
            is DialogType.OnMapClicked -> OnMapClickUi(dialogType.geoPoint, onEvent)
            is DialogType.OnMarkerClick -> OnMarkerClickUi(
                dialogType.marker,
                onEvent
            )
        }

    }
}

@Composable
fun OnMapClickUi(point: GeoPoint, onEvent: (MapEvent) -> Unit) {
    Text(
        text = stringResource(R.string.add_new_point),
        modifier = Modifier
            .fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium
    )

    var markerName by remember {
        mutableStateOf("")
    }
    NameInputField(markerName, point, onEvent) { newValue ->
        markerName = newValue
    }

    Button(
        onClick = {
            onEvent(NewMarkerAdded(markerName, point))
        },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = markerName.length in NameMinLength..NameMaxLength,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = stringResource(R.string.done),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private const val NameMaxLength = 40
private const val NameMinLength = 1

@SuppressLint("MissingPermission") //no it is not
@Composable
fun OnMarkerClickUi(marker: Marker, onEvent: (MapEvent) -> Unit) {
    //хотел сделать выбор старт поинта, но времени не хватило
    val fusedLocationClient = koinInject<FusedLocationProviderClient>()

    Image(
        bitmap = marker.icon.toBitmap().asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
            .padding(vertical = 8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                onEvent(RemoveMarkerClicked(marker))
            },
            modifier = Modifier
                .weight(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.remove),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Button(
            onClick = {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    onEvent(
                        GetDirectionClicked(
                            startPoint = GeoPoint(location.latitude, location.longitude),
                            endPoint = marker.position
                        )
                    )
                }
            },
            modifier = Modifier
                .weight(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.direction),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


@Composable
private fun NameInputField(
    name: String,
    point: GeoPoint,
    onEvent: (MapEvent) -> Unit,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = name,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.name),
                style = MaterialTheme.typography.bodySmall
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = when {
                name.length in NameMinLength..NameMaxLength -> ImeAction.Done
                else -> ImeAction.None
            }
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onEvent(NewMarkerAdded(name, point))
            }
        ),
        isError = name.length > NameMaxLength || name.length < NameMinLength,
        supportingText = {
            Text(text = stringResource(R.string.counter_with_slash, name.length, NameMaxLength))
        },
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            focusedSupportingTextColor = MaterialTheme.colorScheme.primary,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.primary.copy(0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}