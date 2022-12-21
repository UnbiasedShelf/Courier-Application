package by.bstu.vs.stpms.courier_application.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import by.bstu.vs.stpms.courier_application.R

sealed class CourierTab(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Available : CourierTab("available", R.string.available_orders, R.drawable.ic_home_black_24dp)
    object Active : CourierTab("active", R.string.active_orders, R.drawable.ic_baseline_checklist_24)
    object Profile : CourierTab("courierprofile", R.string.title_profile, R.drawable.ic_baseline_person_24)
}

sealed class CustomerTab(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Created : CourierTab("created", R.string.created_orders, R.drawable.ic_home_black_24dp)
    object History : CourierTab("history", R.string.history, R.drawable.ic_baseline_history_24)
    object Profile : CourierTab("customerprofile", R.string.title_profile, R.drawable.ic_baseline_person_24)
}