package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class EditState(
    var requestEdit: Boolean,
) {
    fun showMainmenu(): Boolean {
        return !requestEdit
    }
}


@ExperimentalMaterialApi
@Composable
fun HostComposable(
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        var currentEditState by remember {
            mutableStateOf(
                EditState(
                    requestEdit = false,
                )
            )
        }

        val goToMainMenuInput = {
            currentEditState = EditState(
                requestEdit = false
            )
        }
        when {
            currentEditState.showMainmenu() -> {
                MenuComposable(
                    bottomSheetState = bottomSheetState,
                    onClickEdit = {
                        currentEditState = EditState(requestEdit = true)
                    }
                )
            }
            currentEditState.requestEdit -> {
                EditComposable(
                    onSubmit = { input ->
                        // save
                        goToMainMenuInput()
                    }
                )
            }

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MenuComposable(
    bottomSheetState: ModalBottomSheetState,
    onClickEdit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Surface(Modifier.fillMaxSize()) {
            ModalBottomSheetLayout(
                scrimColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.32f),
                sheetShape = RoundedCornerShape(
                    topLeft = 16.dp,
                    topRight = 16.dp,
                    bottomRight = 0.dp,
                    bottomLeft = 0.dp
                ),
                sheetState = bottomSheetState,
                sheetContent = {
                    SheetContentComposable()
                }
            ) {
                Column() {
                    Button(
                        onClick = onClickEdit,
                        modifier = Modifier.padding(top = 50.dp)
                    ) {
                        Text(text = "Edit")
                    }

                    Button(
                        onClick = onClickEdit,
                        modifier = Modifier.padding(top = 50.dp)
                    ) {
                        Text(text = "Show sheet")
                    }
                }
            }

        }
    }
}

@Composable
fun EditComposable(
    onSubmit: (name: String) -> Unit
) {
    var input by remember { mutableStateOf(TextFieldValue()) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        OutlinedTextField(
            value = input,
            onValueChange = { text ->
                if (text.text.length < 15 && text.text.count { it == '\n' } < 1) { // single line only
                    input = text
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onImeActionPerformed = { _: ImeAction, s: SoftwareKeyboardController? ->
                s?.showSoftwareKeyboard()
            },
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        Button(
            onClick = {
                onSubmit(input.text)
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit")
        }

    }
}


@Composable
fun SheetContentComposable(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)
    ) {
        Text("This shouldnt be seen")
    }

}