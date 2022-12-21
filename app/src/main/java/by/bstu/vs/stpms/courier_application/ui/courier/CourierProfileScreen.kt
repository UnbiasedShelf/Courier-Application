package by.bstu.vs.stpms.courier_application.ui.courier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Stats
import by.bstu.vs.stpms.courier_application.ui.composables.LabelText
import by.bstu.vs.stpms.courier_application.ui.composables.PlaceholderCard
import by.bstu.vs.stpms.courier_application.ui.composables.UserCard
import by.bstu.vs.stpms.courier_application.ui.courier.model.CourierProfileViewModel
import by.bstu.vs.stpms.courier_application.ui.util.Util
import by.bstu.vs.stpms.courier_application.ui.util.YAxisDrawer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun CourierProfileScreen(
    toLogin: () -> Unit,
    toCustomer: () -> Unit,
    viewModel: CourierProfileViewModel = viewModel()
) {
    LaunchedEffect(true) {
        if (viewModel.user == null && viewModel.stats == null)
            viewModel.initialLoad()
    }
    Scaffold {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.isRefreshing),
            onRefresh = { viewModel.refresh() }
        ) {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                UserCard(
                    user = viewModel.user,
                    isVerified = viewModel.isVerified(viewModel.user?.roles),
                    isCourier = true,
                    modifier = Modifier.padding(top = 16.dp)
                )
                StatsCard(
                    stats = viewModel.stats,
                    deliveredMap = viewModel.deliveredMap,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.logout()
                        toLogin()
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = { toCustomer() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.switch_to_customer),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun StatsCard(
    stats: Stats?,
    deliveredMap: Map<String, Float>?,
    modifier: Modifier = Modifier,
) {
    PlaceholderCard(
        item = stats,
        modifier = modifier
    ) {
        if (stats != null) {
            Column {
                Text(
                    text = stringResource(id = R.string.statistics),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                LabelText(
                    label = stringResource(id = R.string.delivered_orders),
                    text = "${stats.deliveredOrdersCount}"
                )
                LabelText(
                    label = stringResource(id = R.string.delivered_orders_in_time),
                    text = "${stats.deliveredInTimeCount}"
                )
                LabelText(
                    label = stringResource(id = R.string.total_delivered_price),
                    text = "${Util.formatDouble(
                        stats.deliveredTotalPrice
                    )} ${stringResource(id = R.string.dollar)}"
                )
                LabelText(
                    label = stringResource(id = R.string.delivered_products),
                    text = "${stats.deliveredProductsCount}"
                )
            }
        }
    }
    PlaceholderCard(
        item = stats,
        modifier = modifier
    ) {
        if (stats != null) {
            Column {
                Text(
                    text = stringResource(R.string.total_delivered),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    BarChart(
                        barChartData = BarChartData(
                            listOf(
                                BarChartData.Bar(
                                    stats.deliveredOrdersCount.toFloat(),
                                    MaterialTheme.colors.primary,
                                    stringResource(id = R.string.delivered_orders)
                                ),
                                BarChartData.Bar(
                                    stats.deliveredInTimeCount.toFloat(),
                                    MaterialTheme.colors.primary,
                                    stringResource(id = R.string.delivered_orders_in_time)
                                )
                            ),
                            padBy = 0f
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        animation = simpleChartAnimation(),
                        barDrawer = SimpleBarDrawer(),
                        xAxisDrawer = me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer(),
                        yAxisDrawer = YAxisDrawer(
                            drawLabelEvery = 1,
                            labelValueFormatter = { it.toInt().toString() },
                            labelTextColor = MaterialTheme.colors.onSurface
                        ),
                    )
                }
            }
        }
    }
    PlaceholderCard(
        item = deliveredMap,
        modifier = modifier
    ) {
        if (deliveredMap != null) {
            Column {
                Text(
                    text = stringResource(R.string.delivered_30),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    LineChart(
                        lineChartData = LineChartData(
                            points = deliveredMap.map { LineChartData.Point(it.value, it.key) },
                            padBy = 0f
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        animation = simpleChartAnimation(),
                        pointDrawer = FilledCircularPointDrawer(diameter = 6.dp, color = MaterialTheme.colors.primary),
                        lineDrawer = SolidLineDrawer(color = MaterialTheme.colors.primary),
                        xAxisDrawer = SimpleXAxisDrawer(
                            drawLabelEvery = 5,
                            labelTextColor = MaterialTheme.colors.onSurface
                        ),
                        yAxisDrawer = YAxisDrawer(
                            drawLabelEvery = 1,
                            labelValueFormatter = { it.toInt().toString() },
                            labelTextColor = MaterialTheme.colors.onSurface
                        ),
                    )
                }
            }
        }
    }
}