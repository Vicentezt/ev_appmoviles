package com.example.dsy1105_004v2026.ui.theme

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dsy1105_004v2026.R
import com.example.dsy1105_004v2026.ui.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    //navegar entre paginas
    navController: NavController,
    vm: LoginViewModel= viewModel()

){
    val state=vm.uiState
    var showPass by remember { mutableStateOf(false) }

    val ColorScheme = lightColorScheme(
        primary = AzulPrimario,
        onPrimary = Color.White,
        onSurface = Color(0xFF333333), //gris
    )//fin colores

    MaterialTheme(
        colorScheme = ColorScheme
    ){// inicio aplicacion Material



    //Scaffold genera una pantalla basica
    Scaffold(
        topBar = {
            TopAppBar(title ={Text("Agencia Convenios",
            color= MaterialTheme.colorScheme.onPrimary,
            )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ) )
        }
    ){innerPadding ->
        Column(
           modifier= Modifier
               .padding(innerPadding)
               .fillMaxSize()
               .background(FondoClaro)
               .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally



        ){//Inicio columna

            Text("¡Bienvenido a Agencia Convenios!",
                style=MaterialTheme.typography.headlineMedium,
                color= MaterialTheme.colorScheme.primary

            )

            Spacer(modifier=Modifier.height(24.dp))


            Image(
                painter= painterResource(id = R.drawable.logo_agencia),
                contentDescription = "Logo Agencia Convenios",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit

            )
            Spacer(modifier=Modifier.height(24.dp))

            OutlinedTextField(
                value=state.username,
                onValueChange = vm::onUsernameChange,
                label={Text("Usuario")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.95f)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = vm::onPasswordChange,
                label={Text("Contraseña")},
                singleLine = true,
                visualTransformation=if (showPass)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick={showPass=!showPass}){
                        Text(if (showPass) "Ocultar" else "Ver")
                    }//fin Icon
                }, //fin trail
                modifier= Modifier.fillMaxWidth(0.95f)
            )// fin pass

            if(state.error !=null){
                Spacer(Modifier.height(8.dp))
                Text(
                    text=state.error ?:"",  // is un the y else
                    color=MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )

            }


            Button(onClick={/* accion futura */
            vm.submit { user ->
//                navController.navigate("muestraDatos/$user")

                navController.navigate("DrawerMenu/$user")



                {// inicio navegacion
                    popUpTo("login") {inclusive=true}  // no permita volver
                    launchSingleTop=true
                }//termino navegacion
            }//fin submit

            },
                enabled=!state.isLoading,
                modifier = Modifier.fillMaxWidth(0.7f).height(52.dp)

            ){
               // Text("Presioname")
Text(if(state.isLoading)  "Validando"  else "Inicia sesion")

            }


        }//Fin Inicio columna

    }//fin inner
    }// fin aplicacion Material
}//fin HomeScreen

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
// Crear NavController para simular el despliege
    val navController = rememberNavController()
    //vamos a simular el viewmodel
val vm= LoginViewModel()

    HomeScreen(navController=navController, vm=vm)
}




