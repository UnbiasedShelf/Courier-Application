package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String = "",
    isReadOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    enableFocus: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var wasInFocus by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 8.dp)) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = if (enableFocus) ImeAction.Next else ImeAction.Default
            ),
            keyboardActions =
            if (enableFocus) KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            else KeyboardActions.Default,
            singleLine = singleLine,
            maxLines = maxLines,
            label = { Text(text = labelText) },
            isError = isError && wasInFocus,
            readOnly = isReadOnly,
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (trailingIcon != null && onTrailingIconClick != null) {
                    IconButton(onClick = {
                        wasInFocus = true
                        onTrailingIconClick()
                    }) {
                        trailingIcon()
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                //.align(Alignment.TopCenter)
                //.padding(bottom = 18.dp)
                .onFocusChanged {
                    if (it.isFocused && !wasInFocus) {
                        wasInFocus = true
                    }
                }
        )

        AnimatedVisibility(
            visible = isError && wasInFocus,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier
                .padding(start = 16.dp)
            //.align(Alignment.BottomStart)
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }


    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ValidatedRowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String = "",
    isReadOnly: Boolean = false
) {
    var wasInFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(top = 8.dp)) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            label = { Text(text = labelText) },
            isError = isError && wasInFocus,
            readOnly = isReadOnly,
            modifier = Modifier
                .fillMaxWidth()
                //.align(Alignment.TopCenter)
                //.padding(bottom = 18.dp)
                .onFocusChanged {
                    if (it.isFocused && !wasInFocus) {
                        wasInFocus = true
                    }
                }
        )

        AnimatedVisibility(
            visible = isError && wasInFocus,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier
                .padding(start = 16.dp)
            //.align(Alignment.BottomStart)
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }


    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ValidatedPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var wasInFocus by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 8.dp)) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector = image, "")
                }
            },
            singleLine = true,
            label = { Text(text = labelText) },
            isError = isError && wasInFocus,
            modifier = modifier
                .fillMaxWidth()
                //.align(Alignment.TopCenter)
                //.padding(bottom = 18.dp)
                .onFocusChanged {
                    if (it.isFocused && !wasInFocus) {
                        wasInFocus = true
                    }
                }
        )

        AnimatedVisibility(
            visible = isError && wasInFocus,
            enter = slideInVertically(),
            exit = slideOutVertically(),
            modifier = Modifier
                .padding(start = 16.dp)
            //.align(Alignment.BottomStart)
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }


    }
}