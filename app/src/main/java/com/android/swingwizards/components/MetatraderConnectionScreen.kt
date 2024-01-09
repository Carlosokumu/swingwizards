package com.android.swingwizards.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.swingwizards.R
import com.android.swingwizards.common.AccountPlatform
import com.android.swingwizards.common.AppAlertDialog
import com.android.swingwizards.common.LoginId
import com.android.swingwizards.common.PasswordSection
import com.android.swingwizards.common.ServerSection
import com.android.swingwizards.models.UiState
import com.android.swingwizards.theme.AppTheme
import com.android.swingwizards.viewmodels.MetaTraderViewModel
import com.example.core.network.data.models.MetaAPIResponse
import org.koin.androidx.compose.getViewModel

@Composable
fun MetaTraderConnectionScreen(navController: NavController) {

    val metaTraderViewModel: MetaTraderViewModel = getViewModel()
    val metaTraderLogin =
        metaTraderViewModel.metaTraderFields.collectAsState().value.metaTraderLogin
    val metaTraderPassword =
        metaTraderViewModel.metaTraderFields.collectAsState().value.metaTraderPassword
    val openAlertDialog = remember { mutableStateOf(false) }
    val openDialogInfo =
        remember {
            mutableStateOf(
                DialogInfo(
                    message = "",
                    shouldOpen = false,
                    description = "",
                    failed = false
                )
            )
        }
    val selectedServer = remember { mutableStateOf("") }
    val selectedPlatform = remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    val uiState: UiState by metaTraderViewModel.uiState.collectAsState(initial = UiState.Relaxed)
    when (uiState) {
        is UiState.Relaxed -> {
        }

        is UiState.Loading -> {
            isLoading = true
        }

        is UiState.ServerError -> {
            isLoading = false
            LaunchedEffect(key1 = uiState, block = {
                openDialogInfo.value = DialogInfo(
                    message = "Error",
                    shouldOpen = true,
                    description = (uiState as UiState.ServerError).message,
                    failed = true
                )
            })
        }

        is UiState.Success<*> -> {
            isLoading = false
            val successResponse = (uiState as UiState.Success<MetaAPIResponse>).response
            metaTraderViewModel.saveUserAccountId(successResponse.deployedAccount.accountId)
            LaunchedEffect(key1 = uiState, block = {
                openDialogInfo.value = DialogInfo(
                    message = "Success",
                    shouldOpen = true,
                    description = "Account connected successfully",
                    failed = false
                )
            })

        }

        is UiState.Error -> {
            isLoading = false
            LaunchedEffect(key1 = uiState, block = {
                openDialogInfo.value = DialogInfo(
                    message = "Error",
                    shouldOpen = true,
                    description = "Please check your internet connection and try again!",
                    failed = true
                )
            })
        }

    }
    when {
        openDialogInfo.value.shouldOpen -> {
            AppAlertDialog(
                onDismissRequest = {
                    openDialogInfo.value =
                        DialogInfo(
                            message = "",
                            shouldOpen = false,
                            description = "",
                            failed = false
                        )
                },
                confirmText = if (openDialogInfo.value.failed) "Try Again" else "Ok,Great",
                onConfirmation = {
                    openAlertDialog.value = false
                    navController.navigate("home")

                },
                isFailed = openDialogInfo.value.failed,
                dialogTitle = if (openDialogInfo.value.failed) "Error" else "Success",
                dialogText = openDialogInfo.value.description,
                icon = Icons.Default.Done
            )
        }
    }




    Scaffold(
        topBar = {
            SignInTopBar {

            }
        },
        modifier = Modifier
            .background(AppTheme.colors.background)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                    .background(AppTheme.colors.background)
            ) {
                Text(
                    text = stringResource(id = R.string.connect_metatrader),
                    style = AppTheme.typography.subtitle,
                    color = AppTheme.colors.textPrimary,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = stringResource(R.string.metatrader_rationale),
                    style = AppTheme.typography.caption,
                    color = AppTheme.colors.textPrimary
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                    .background(AppTheme.colors.background)
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                Spacer(modifier = Modifier.height(15.dp))
                LoginId(loginId = metaTraderLogin, updateLoginId = { loginId ->
                    metaTraderViewModel.onLoginEntered(loginId)
                })
                Spacer(modifier = Modifier.height(15.dp))
                PasswordSection(updatePassword = { password ->
                    metaTraderViewModel.onPasswordEntered(password)
                }, password = metaTraderPassword, shouldShowIcon = false)
                Spacer(modifier = Modifier.height(15.dp))
                AccountPlatform(selectedItem = { platform ->
                    selectedPlatform.value = platform

                })
                Spacer(modifier = Modifier.height(15.dp))
                ServerSection(selectedItem = { server ->
                    selectedServer.value = server
                })
                Spacer(modifier = Modifier.height(15.dp))

            }
            AppButton(isLoading = isLoading, text = "Connect account", onButtonClick = {
                metaTraderViewModel.subMitDetails(
                    metaTraderPassword = metaTraderPassword,
                    metaTraderLogin = metaTraderLogin,
                    server = selectedServer.value,
                    metaTraderVersion = selectedPlatform.value
                )
            }, enabled = metaTraderLogin != "" && metaTraderPassword != "")
        }
    }
}


data class DialogInfo(
    var message: String,
    var shouldOpen: Boolean,
    var description: String,
    var failed: Boolean
)

