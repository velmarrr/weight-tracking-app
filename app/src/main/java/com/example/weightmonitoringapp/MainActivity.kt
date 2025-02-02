package com.example.weightmonitoringapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.weightmonitoringapp.db.Records
import com.example.weightmonitoringapp.db.RecordsDatabase
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weightmonitoringapp.viewmodels.RecordsViewModel
import com.example.weightmonitoringapp.viewmodels.RecordsViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: RecordsViewModel by viewModels {
        RecordsViewModelFactory(RecordsDatabase.getInstance(this).getRecordsDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(viewModel)
        }
    }
}

@Composable
fun App(viewModel: RecordsViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel = viewModel, navController = navController)
        }
        composable("add_record") {
            AddRecordScreen(onAdd = { weight ->
                viewModel.addRecord(weight)
                navController.popBackStack()
            }, navController = navController)
        }
    }
}

@Composable
fun MainScreen(viewModel: RecordsViewModel, navController: NavController) {
    val records by viewModel.records.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items = records) { record ->
                RecordsItem(record = record, viewModel = viewModel)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = { navController.navigate("add_record") },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF78745C)
            ),
            modifier = Modifier.size(60.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(
                "+",
                fontFamily = FontFamily(Font(R.font.varela_round)),
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFF4EFEF)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AddRecordScreen(onAdd: (Float) -> Unit, navController: NavController) {
    var weightText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.offset(y = (-150).dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = {
                        weightText = it
                        isError = it.toFloatOrNull() == null || it.toFloatOrNull()!! < 0 ||
                                it.toFloatOrNull()!! > 200
                    },
                    label = {
                        Text(
                            "Enter your weight",
                            fontFamily = FontFamily(Font(R.font.varela_round)),
                            fontSize = 18.sp,
                            color = if (isError) Color(0xFF9E0E0E) else Color(0xFF78745C)
                        )
                    },
                    isError = isError,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.varela_round)),
                        fontSize = 18.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF78745C)
                    ),
                    modifier = Modifier.width(270.dp)
                )
                if (isError) {
                    Text(
                        "Invalid weight",
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.varela_round)),
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        val weight = weightText.toFloatOrNull()
                        if (weight != null && weight <= 200) {
                            onAdd(weight)
                        }
                    },
                    modifier = Modifier.width(270.dp).height(51.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF78745C)
                    ),
                    enabled = !isError && weightText.isNotEmpty()
                ) {
                    Text(
                        "SAVE",
                        fontFamily = FontFamily(Font(R.font.varela_round)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF4EFEF)
                    )
                }
            }
        }
    }
}

@Composable
fun RecordsItem(record: Records, viewModel: RecordsViewModel) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().height(115.dp),
        shape = RoundedCornerShape(CornerSize(20.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0DBC0)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        ) {
            Text(
                text = "${record.weight} kg",
                modifier = Modifier.weight(1f),
                fontFamily = FontFamily(Font(R.font.varela_round)),
                fontSize = 24.sp
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(Date(record.date)),
                    fontFamily = FontFamily(Font(R.font.varela_round)),
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier.size(48.dp).clip(RectangleShape)
                        .background(Color.Transparent).clickable { viewModel.deleteRecord(record) }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.trash),
                        contentDescription = "Delete",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}