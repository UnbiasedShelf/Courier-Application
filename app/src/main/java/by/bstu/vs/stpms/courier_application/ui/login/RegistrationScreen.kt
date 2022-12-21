package by.bstu.vs.stpms.courier_application.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.ui.composables.*
import by.bstu.vs.stpms.courier_application.ui.login.model.RegistrationViewModel
import by.bstu.vs.stpms.courier_application.ui.util.MaskVisualTransformation
import by.bstu.vs.stpms.courier_application.ui.util.RegistrationValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    viewModel: RegistrationViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.registration)) },
                navigationIcon = { BackButton(navController = navController) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            with(viewModel) {

                ValidatedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    labelText = stringResource(id = R.string.first_name),
                    isError = !RegistrationValidator.isNameValid(firstName),
                    errorMessage = stringResource(R.string.name_length_message)
                )
                ValidatedTextField(
                    value = secondName,
                    onValueChange = { secondName = it },
                    labelText = stringResource(id = R.string.second_name),
                    isError = !RegistrationValidator.isNameValid(secondName),
                    errorMessage = stringResource(R.string.name_length_message)
                )
                ValidatedTextField(
                    value = email,
                    onValueChange = { email = it },
                    labelText = stringResource(id = R.string.email),
                    isError = !RegistrationValidator.isEmailValid(email),
                    errorMessage = stringResource(R.string.email_message),
                    keyboardType = KeyboardType.Email
                )
                ValidatedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.length < 10 && it.all { char -> char.isDigit() }) {
                            phone = it
                        }
                    },
                    labelText = stringResource(id = R.string.phone_number),
                    isError = !RegistrationValidator.isPhoneValid(phone),
                    errorMessage = stringResource(R.string.phone_message),
                    keyboardType = KeyboardType.Phone,
                    visualTransformation = MaskVisualTransformation("+375 (##) ###-##-##")
                )
                ValidatedPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    labelText = stringResource(id = R.string.password),
                    isError = !RegistrationValidator.isPasswordValid(password),
                    errorMessage = stringResource(R.string.password_message)
                )
                ValidatedPasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    labelText = stringResource(id = R.string.confirm_password),
                    isError = !RegistrationValidator.isConfirmPasswordValid(
                        password,
                        confirmPassword
                    ),
                    errorMessage = stringResource(R.string.confirm_password_message)
                )


                var expanded by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                ) {
                    Checkbox(
                        checked = expanded,
                        onCheckedChange = null,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(text = stringResource(R.string.sign_up_as_courier))
                }

                LaunchedEffect(expanded) {
                    courierType = if (expanded) CourierType.Walk else CourierType.NotCourier
                }

                Card(
                    shape = RoundedCornerShape(4.dp),
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        CourierTypeSelector(
                            selectedType = courierType,
                            onTypeChanged = { courierType = it }
                        )
                    }
                }

                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                var isLoading by remember {
                    mutableStateOf(false)
                }
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            val result = signUp()
                            isLoading = false
                            if (result.isSuccessful) {
                                Toast.makeText(context, "Sign up success", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("HTTP", "sign up: success")
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    context,
                                    result.t?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    enabled = RegistrationValidator.isFormValid(
                        firstName,
                        secondName,
                        email,
                        phone,
                        password,
                        confirmPassword
                    ) && !isLoading,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLoading) {
                            ButtonProgress()
                        }
                        Text(
                            text = stringResource(id = R.string.sign_up),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}