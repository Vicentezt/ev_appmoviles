package com.example.dsy1105_004v2026.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable

fun DrawerMenu(
    username:String,
    navController: NavController
)
{//Inicio Drawer

    Column(modifier=Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    { //inicio columna
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.primary)
    )//fin Box
    {
        Text(
            text="Menú : $username",
            style= MaterialTheme.typography.headlineSmall,
            color=MaterialTheme.colorScheme.onPrimary,
            modifier= Modifier
                .align(Alignment.BottomStart)
            .padding(16.dp)
        )
    }

        //Items

        //LazyColumn: Crea una lista de elementos que pueden desplazar

        LazyColumn(modifier=Modifier.weight(1f)) {
            item {
                NavigationDrawerItem(
                    label = { Text("Registro de ventas") },
                    selected = false,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = {
                        navController.navigate("listaVentas")
                    },
                    icon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Registro de ventas"
                        )
                    }
                )
            }//Fin item ventas


            item{
                NavigationDrawerItem(
                    label = {Text ("Clínicas dentales")},
                    selected = false,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick={ navController.navigate("convenio/dental") },
                    icon={Icon(Icons.Default.LocalHospital,
                        contentDescription = "Clínicas dentales"   )}
                )
            }//Fin item dental


            item{
                NavigationDrawerItem(
                    label = {Text ("Servicios automotrices")},
                    selected = false,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick={ navController.navigate("convenio/automotriz") },
                    icon={Icon(Icons.Default.DirectionsCar,
                        contentDescription = "Servicios automotrices"   )}
                )
            }//Fin item automotriz


            item{
                NavigationDrawerItem(
                    label = {Text ("Servicios veterinarios")},
                    selected = false,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick={ navController.navigate("convenio/veterinario") },
                    icon={Icon(Icons.Default.Pets,
                        contentDescription = "Servicios veterinarios"   )}
                )
            }//Fin item veterinario








        }//Fin Lazy Columnm






    }// fin columna
}//fin Drawer



@Preview(showBackground = true)
@Composable


fun DrawerMenuPreview(){
    val navController = rememberNavController()
    DrawerMenu(username = "Usuario Prueba", navController=navController)

}//fin drawerpreview
