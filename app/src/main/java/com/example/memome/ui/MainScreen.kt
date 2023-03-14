package com.example.memome.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.*

data class BottomMenuItem(val label: String, val icon: ImageVector)
data class Memo(var title: String, var memo: String, var selected: Boolean)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyMainScreen() {
    val scaffoldState = rememberScaffoldState()
    var memos = (1..10).map {
        Memo(
            title = "title ${it.toString()}",
            memo = "memo ${it.toString()}",
            selected = false
        )
    }.toMutableList()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = { MyBottomAppBar() },
        scaffoldState = scaffoldState
    ) {
        val paddingOfLeftColumn = PaddingValues(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 0.dp)
        val paddingOfRightColumn = PaddingValues(start = 8.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
        val reorderState = rememberReorderState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues = it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*
            LazyVerticalGrid(
                state = reorderState.,
                columns = GridCells.Fixed(2),
                content = {
                    items(memos.size) { index ->
                        Card(
                            backgroundColor = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    paddingValues = if (index % 2 == 0) paddingOfLeftColumn else paddingOfRightColumn
                                )
                                .aspectRatio(1F)
                                .border(
                                    width = 5.dp,
                                    color = Color.White,
                                ),
                            elevation = 8.dp,
                            onClick = { memos[index].selected = true }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(7.dp)
                            ) {
                                Text(text = memos[index].title)
                                Text(text = memos[index].memo)
                                Text(text = if (memos[index].selected) "Selected" else "")
                            }
                        }

                    }
                }
            )
             */
            LazyColumn(
                state = reorderState.listState,
                modifier = Modifier
                    .reorderable(
                        state = reorderState,
                        onMove = { from, to ->
                            memos.move(from.index, to.index)
                        }
                    )
            ) {
                items(memos.size) { index ->
                         Card(
                            backgroundColor = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    top = 10.dp,
                                    end = 10.dp,
                                    bottom = 0.dp,
                                    //paddingValues = if (index % 2 == 0) paddingOfLeftColumn else paddingOfRightColumn
                                )
                                .clickable {}
                                .detectReorderAfterLongPress(reorderState)
                                .draggedItem(reorderState.offsetByIndex(index))
                                .aspectRatio(1F)
                                .border(
                                    width = 5.dp,
                                    color = Color.White,
                                ),
                            elevation = 8.dp,
                            onClick = { memos[index].selected = true }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(7.dp)
                            ) {
                                Text(text = memos[index].title)
                                Text(text = memos[index].memo)
                                Text(text = if (memos[index].selected) "Selected" else "")
                            }
                        }
                }

            }
        }
    }
}

@Composable
fun MyTopAppBar() {
    var text by remember { mutableStateOf("") }
    val shape = remember { RoundedCornerShape(25.dp) }
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
    ) {
        OutlinedTextField(
            value = text,
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = Color.Gray,
                    shape = shape
                )
            ,
            onValueChange = { },
            placeholder = { Text(text = "Placeholder") }
            //leadingIcon = {
            //    Icon(
            //        imageVector = Icons.Filled.Search,
            //        contentDescription = null,
            //    )
            //}
        )
    }
}

@Composable
fun MyBottomAppBar() {
    val bottomMenuItemsList = prepareBottomMenu()
    val contextForToast = LocalContext.current.applicationContext
    var selectedItem by remember {
        mutableStateOf("Home")
    }

    BottomAppBar(
        cutoutShape = CircleShape
    ) {
        bottomMenuItemsList.forEachIndexed { index, menuItem ->
            BottomNavigationItem(
                selected = (selectedItem == menuItem.label),
                onClick = {
                    selectedItem = menuItem.label
                    Toast.makeText(
                        contextForToast,
                        menuItem.label,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                icon = {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = menuItem.label
                    )
                },
                label = {
                    Text(text = menuItem.label)
                },
                enabled = true
            )
        }
    }
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuListItem = arrayListOf<BottomMenuItem>()

    bottomMenuListItem.add(BottomMenuItem(label = "Home", icon = Icons.Filled.Home))
    bottomMenuListItem.add(BottomMenuItem(label = "Add", icon = Icons.Filled.Add))
    bottomMenuListItem.add(BottomMenuItem(label = "Settings", icon = Icons.Filled.Settings))

    return bottomMenuListItem
}

@Preview
@Composable
fun PreviewMyMainScreen() {
    MyMainScreen()
}

@Preview
@Composable
fun PreviewMyTopAppBar() {
    MyTopAppBar()
}

@Preview
@Composable
fun PreviewMyBottomAppBar() {
    MyBottomAppBar()
}