package com.android.swingwizards.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.swingwizards.R
import com.android.swingwizards.enums.PeriodHistoryRange
import com.android.swingwizards.enums.PeriodRange
import com.android.swingwizards.theme.AppTheme


@Composable
fun AccountPlatform(
    modifier: Modifier = Modifier,
    selectedItem: (String) -> Unit
) {
    Column(modifier = modifier) {
        androidx.compose.material.Text(
            text = stringResource(id = R.string.mt_version),
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.subtitle
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenu(items = listOf("mt4", "mt5"), selectedItem)
    }
}

@Composable
fun ServerSection(
    modifier: Modifier = Modifier,
    selectedItem: (String) -> Unit,
    servers: List<String>,
    searchQuery: (String) -> Unit
) {
    Column(modifier = modifier) {
        androidx.compose.material.Text(
            text = stringResource(id = R.string.server),
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.subtitle
        )
        Spacer(modifier = Modifier.height(10.dp))
        SearchDropdownMenu(
            servers,
            selectedItem,
            searchQuery
        )
    }
}


@Composable
fun TradeHistoryPeriod(
    modifier: Modifier = Modifier,
    selectedPeriod: (PeriodRange) -> Unit,
    shouldExpand: Boolean,
    shouldClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(30.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {

        androidx.compose.material3.DropdownMenu(
            expanded = shouldExpand,
            onDismissRequest = {
                shouldClose()
            },
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .padding(horizontal = 15.dp)
        ) {
            PeriodHistoryRange.values().forEach { period ->
                DropdownMenuItem(
                    modifier = Modifier
                        .background(AppTheme.colors.onPrimary)
                        .padding(),
                    text = {
                        Text(
                            text = period.uiString,
                            style = AppTheme.typography.caption,
                            color = AppTheme.colors.textPrimary
                        )
                    },
                    onClick = {
                        selectedPeriod(period.periodRange)
                        shouldClose()
                    },
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(items: List<String>, selectedItem: (String) -> Unit) {
    LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items[0]) }


    selectedItem(selectedText)

    Surface(
        color = AppTheme.colors.onPrimary,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(AppTheme.colors.onPrimary),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                modifier = Modifier.background(AppTheme.colors.onPrimary),
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .focusProperties {
                            canFocus = false
                        }
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = AppTheme.colors.secondaryVariant,
                    ),
                    textStyle = AppTheme.typography.caption,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.None,
                    )

                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    modifier = Modifier.background(AppTheme.colors.onPrimary),
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    style = AppTheme.typography.caption,
                                    color = AppTheme.colors.textPrimary
                                )
                            },
                            modifier = Modifier.background(AppTheme.colors.onPrimary),
                            onClick = {
                                selectedText = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDropdownMenu(
    items: List<String>,
    selectedItem: (String) -> Unit,
    searchQuery: (String) -> Unit
) {
    LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var canFocus by remember { mutableStateOf(true) }
    var expandedDropDownMenu by remember {
        mutableStateOf(true)
    }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }

    var filteredItems by remember { mutableStateOf(items) }

    // Filter items based on user input
    val filteredList = items.filter { item ->
        item.lowercase().contains(selectedOptionText.lowercase()) // Case-insensitive search
    }
    filteredItems = filteredList
    Surface(
        color = AppTheme.colors.onPrimary,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(AppTheme.colors.onPrimary),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                modifier = Modifier.background(AppTheme.colors.onPrimary),
                onExpandedChange = {
                    expanded = !expanded
                    expandedDropDownMenu = it
                    canFocus = true
                },
            ) {
                TextField(
                    value = selectedOptionText,
                    onValueChange = { value ->
                        selectedOptionText = value
                        if (value.isNotEmpty()) {
                            expandedDropDownMenu = true
                        }
                        searchQuery(value)
                    },
                    readOnly = false,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    placeholder = {
                        Text(
                            text = "Search for broker",
                            color = AppTheme.colors.textPrimary,
                            style = AppTheme.typography.subtitle
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .focusProperties {
                            this.canFocus = canFocus
                        }
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = AppTheme.colors.secondaryVariant,
                    ),
                    textStyle = AppTheme.typography.caption,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.None,
                    )

                )
                if (filteredItems.isNotEmpty() && selectedOptionText.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedDropDownMenu,
                        modifier = Modifier.background(AppTheme.colors.onPrimary),
                        onDismissRequest = { expandedDropDownMenu = false }
                    ) {
                        filteredItems.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item,
                                        style = AppTheme.typography.caption,
                                        color = AppTheme.colors.textPrimary
                                    )
                                },
                                modifier = Modifier.background(AppTheme.colors.onPrimary),
                                onClick = {
                                    selectedOptionText = item
                                    selectedItem(item)
                                    expandedDropDownMenu = false
                                    canFocus = false
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DropDownMenuPreview() {
    SearchDropdownMenu(listOf(), selectedItem = {}, searchQuery = {})
}