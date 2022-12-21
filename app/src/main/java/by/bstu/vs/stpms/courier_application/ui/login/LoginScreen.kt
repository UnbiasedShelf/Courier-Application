package by.bstu.vs.stpms.courier_application.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.ui.composables.ButtonProgress
import by.bstu.vs.stpms.courier_application.ui.login.model.AuthViewModel
import by.bstu.vs.stpms.courier_application.ui.util.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel(),
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.login)) })
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val focusManager = LocalFocusManager.current

            Image(
                painter = painterResource(id = R.drawable.ic_express_delivery),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(top = 36.dp)
                    .size(120.dp)
            )

            OutlinedTextField(
                value = viewModel.login,
                onValueChange = { viewModel.login = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            var passwordVisibility by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                singleLine = true,
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
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            var isLoading by remember { mutableStateOf(false) }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        scope.launch {
                            isLoading = true
                            val result = viewModel.login()
                            isLoading = false
                            if (result.isSuccessful && result.data != null) {
                                val user = result.data
                                navController.navigate(
                                    if (user.courierType == CourierType.NotCourier)
                                        Route.CUSTOMER
                                    else
                                        Route.COURIER
                                ) {
                                    popUpTo(Route.LOGIN) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    result.t?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLoading) {
                            ButtonProgress()
                        }
                        Text(
                            text = stringResource(id = R.string.login),
                            fontSize = 16.sp
                        )
                    }
                }
                Button(
                    enabled = !isLoading,
                    onClick = {
                        navController.navigate(Route.REGISTRATION)
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.registration),
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}