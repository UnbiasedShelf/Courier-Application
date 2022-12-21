package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.User

@Composable
fun UserCard(
    user: User?,
    isVerified: Boolean,
    isCourier: Boolean,
    modifier: Modifier = Modifier,
    heightIn: Dp = 150.dp,
) {
    PlaceholderCard(
        item = user,
        heightIn = heightIn,
        modifier = modifier
    ) {
        if (user != null) {
            Column {
                Text(
                    text = stringResource(id = R.string.profile_info),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                IconText(
                    text = "${user.firstName} ${user.secondName}"
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_person_16),
                        contentDescription = "name"
                    )
                }
                IconText(
                    text = user.phone
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_phone_16),
                        contentDescription = "phone"
                    )
                }
                IconText(
                    text = user.email
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_email_16),
                        contentDescription = "email"
                    )
                }
                if (isCourier) {
                    IconText(
                        text = stringResource(
                            id = if (isVerified) R.string.verified
                            else R.string.unverified
                        )
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isVerified) R.drawable.ic_baseline_verified_16
                                else R.drawable.ic_baseline_not_verified_16
                            ),
                            tint = if (isVerified) Color.Green else Color.Red,
                            contentDescription = "is verified"
                        )
                    }
                }
            }
        }
    }
}