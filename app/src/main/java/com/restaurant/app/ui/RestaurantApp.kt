package com.restaurant.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class CustomerTab(val title: String, val icon: ImageVector) {
    Home("Асосӣ", Icons.Default.Home), Menu("Меню", Icons.Default.RestaurantMenu),
    Cart("Сабад", Icons.Default.ShoppingBag), Booking("Ҷадвал", Icons.Default.TableBar), Profile("Профил", Icons.Default.Person)
}

private enum class AdminTab(val title: String, val icon: ImageVector) {
    Dashboard("Омор", Icons.Default.Dashboard), Orders("Фармоишҳо", Icons.Default.ReceiptLong),
    Tables("Мизҳо", Icons.Default.TableRestaurant), Menu("Меню", Icons.Default.RestaurantMenu)
}

@Composable
fun RestaurantApp() {
    var isAdmin by remember { mutableStateOf(false) }
    var customerTab by remember { mutableStateOf(CustomerTab.Home) }
    var adminTab by remember { mutableStateOf(AdminTab.Dashboard) }
    val cart = remember { mutableStateMapOf<String, CartItem>() }

    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { isAdmin = !isAdmin },
                containerColor = if (isAdmin) Color(0xFF23201C) else Color(0xFFFF7A45),
                contentColor = Color.White
            ) { Icon(if (isAdmin) Icons.Default.Person else Icons.Default.AdminPanelSettings, null) }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                val tabs: List<Any> = if (isAdmin) AdminTab.entries else CustomerTab.entries
                tabs.forEach { tab ->
                    val selected = if (isAdmin) tab == adminTab else tab == customerTab
                    val title = if (tab is CustomerTab) tab.title else (tab as AdminTab).title
                    val icon = if (tab is CustomerTab) tab.icon else (tab as AdminTab).icon
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (isAdmin) adminTab = tab as AdminTab else customerTab = tab as CustomerTab
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (!isAdmin && tab == CustomerTab.Cart && cart.values.sumOf { it.quantity } > 0)
                                    Badge { Text("${cart.values.sumOf { it.quantity }}") }
                            }) { Icon(icon, title) }
                        },
                        label = { Text(title) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF7A45), indicatorColor = Color(0xFFFFE2D4), selectedTextColor = Color(0xFFFF7A45))
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(isAdmin, transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(180)) }, label = "role") { admin ->
                if (admin) AdminScreen(adminTab) else CustomerScreen(customerTab, cart)
            }
        }
    }
}

@Composable
private fun CustomerScreen(tab: CustomerTab, cart: MutableMap<String, CartItem>) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFBF7)), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            when (tab) {
                CustomerTab.Home -> HomeScreen(cart)
                CustomerTab.Menu -> MenuScreen(cart)
                CustomerTab.Cart -> CartScreen(cart)
                CustomerTab.Booking -> BookingScreen()
                CustomerTab.Profile -> ProfileScreen()
            }
        }
    }
}

@Composable
private fun TopBar(title: String, subtitle: String? = null, trailing: @Composable (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1C19))
            if (subtitle != null) Text(subtitle, color = Color(0xFF89827A), fontSize = 13.sp)
        }
        trailing?.invoke()
    }
}

@Composable
private fun HomeScreen(cart: MutableMap<String, CartItem>) {
    TopBar("Субҳи хайр! 👋", "Чӣ мехоҳед имрӯз хӯрдан?") { Surface(shape = CircleShape, color = Color(0xFFFFE7DD)) { Text("🍽", modifier = Modifier.padding(12.dp), fontSize = 18.sp) } }
    Card(Modifier.fillMaxWidth().height(165.dp).padding(horizontal = 20.dp), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF23201C))) {
        Row(Modifier.fillMaxSize().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("Фурӯши махсус", color = Color(0xFFFFC96B), fontWeight = FontWeight.Bold); Spacer(Modifier.height(5.dp)); Text("−25%", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Black); Text("барои хӯрокҳои асосӣ", color = Color(0xFFEDE8E2), fontSize = 13.sp) }
            Text("🍜", fontSize = 68.sp)
        }
    }
    Spacer(Modifier.height(24.dp)); SectionTitle("Категорияҳо")
    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(listOf("🍕 Пицца", "🍔 Бургер", "🥗 Салат", "🍰 Десерт")) { category -> FilterChip(selected = category == "🍕 Пицца", onClick = {}, label = { Text(category) }, shape = RoundedCornerShape(16.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFF7A45), selectedLabelColor = Color.White)) }
    }
    Spacer(Modifier.height(22.dp)); SectionTitle("Беҳтарин барномаҳо")
    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(Palette.menu.take(4)) { food -> FoodCard(food, cart) }
    }
}

@Composable
private fun SectionTitle(title: String) { Text(title, Modifier.padding(horizontal = 20.dp, vertical = 8.dp), fontSize = 19.sp, fontWeight = FontWeight.Bold) }

@Composable
private fun MenuScreen(cart: MutableMap<String, CartItem>) {
    TopBar("Меню", "6 табақи хуштаъм")
    var query by remember { mutableStateOf("") }
    OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth().padding(horizontal = 20.dp), placeholder = { Text("Ҷустуҷӯи хӯрок...") }, leadingIcon = { Icon(Icons.Default.Search, null) }, shape = RoundedCornerShape(18.dp), singleLine = true)
    Spacer(Modifier.height(12.dp))
    val filtered = Palette.menu.filter { it.name.contains(query, true) || it.description.contains(query, true) }
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(520.dp).padding(top = 14.dp), contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filtered) { food -> FoodCard(food, cart, compact = true) }
    }
}

@Composable
private fun FoodCard(food: Food, cart: MutableMap<String, CartItem>, compact: Boolean = false) {
    var bump by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (bump) 1.12f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "bump")
    Card(Modifier.then(if (compact) Modifier.fillMaxWidth().height(250.dp) else Modifier.width(180.dp).height(230.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(12.dp)) {
            Box(Modifier.fillMaxWidth().height(112.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFFFF1E8)), contentAlignment = Alignment.Center) { Text(food.emoji, fontSize = 58.sp) }
            Spacer(Modifier.height(8.dp)); Text(food.tag, color = Color(0xFFFF7A45), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(food.name, maxLines = 1, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("${food.price.toInt()} сомонӣ", Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF2F8F71))
                IconButton(onClick = { bump = true; val item = cart[food.id]; if (item == null) cart[food.id] = CartItem(food) else item.quantity++ }, modifier = Modifier.size(38.dp).scale(scale).background(Color(0xFFFF7A45), CircleShape)) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
            }
        }
    }
    LaunchedEffect(bump) { if (bump) { kotlinx.coroutines.delay(180); bump = false } }
}

@Composable
private fun CartScreen(cart: MutableMap<String, CartItem>) {
    TopBar("Сабади ман")
    if (cart.isEmpty()) {
        Column(Modifier.fillMaxWidth().height(520.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Text("🛍️", fontSize = 78.sp); Spacer(Modifier.height(12.dp)); Text("Сабади шумо холӣ аст", fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("Баъзе табақҳоро интихоб кунед", color = Color(0xFF8C8780)) }
    } else {
        val total = cart.values.sumOf { it.food.price * it.quantity }
        cart.values.toList().forEach { item ->
            Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(item.food.emoji, Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFF1E8)).padding(10.dp), fontSize = 30.sp)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) { Text(item.food.name, fontWeight = FontWeight.Bold); Text("${item.food.price.toInt()} сомонӣ", color = Color(0xFF777069)) }
                    IconButton(onClick = { if (item.quantity > 1) item.quantity-- else cart.remove(item.food.id) }) { Icon(Icons.Default.Remove, null) }
                    Text("${item.quantity}", fontWeight = FontWeight.Bold)
                    IconButton(onClick = { item.quantity++ }) { Icon(Icons.Default.Add, null) }
                }
            }
        }
        Card(Modifier.fillMaxWidth().padding(20.dp), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF23201C))) {
            Column(Modifier.padding(18.dp)) { Row(Modifier.fillMaxWidth()) { Text("Ҷамъ", color = Color.White, modifier = Modifier.weight(1f)); Text("${total.toInt()} сомонӣ", color = Color(0xFFFFC96B), fontWeight = FontWeight.Bold) }; Spacer(Modifier.height(14.dp)); Button(onClick = {}, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A45))) { Text("Пардохт кардан") } }
        }
    }
}

@Composable
private fun BookingScreen() {
    TopBar("Брон кардани миз", "Бо як клик")
    var guests by remember { mutableIntStateOf(2) }
    var selectedTime by remember { mutableIntStateOf(1) }
    Card(Modifier.fillMaxWidth().padding(20.dp), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(18.dp)) {
            Text("Шумораи меҳмонон", fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) { (1..6).forEach { n -> FilterChip(selected = guests == n, onClick = { guests = n }, label = { Text("$n") }, shape = CircleShape, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFF7A45), selectedLabelColor = Color.White)) } }
            Text("Вақти мувофиқ", fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { itemsIndexed(listOf("18:00", "18:30", "19:00", "19:30", "20:00")) { index, time -> FilterChip(selected = selectedTime == index, onClick = { selectedTime = index }, label = { Text(time) }) } }
            Spacer(Modifier.height(18.dp)); Button(onClick = {}, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A45))) { Icon(Icons.Default.Check, null); Spacer(Modifier.width(8.dp)); Text("Тасдиқ кардани брон") }
        }
    }
}

@Composable
private fun ProfileScreen() {
    TopBar("Профили ман")
    Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(54.dp, shape = CircleShape, color = Color(0xFFFF7A45)) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("A", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold) } }
        Spacer(Modifier.height(10.dp)); Text("Корбар", fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("0 фармоишҳои анҷомёфта", color = Color(0xFF8C8780))
        Spacer(Modifier.height(24.dp)); listOf(Icons.Default.ReceiptLong to "Фармоишҳои ман", Icons.Default.Favorite to "Табақҳои дӯстдошта", Icons.Default.LocationOn to "Суроғаҳо", Icons.Default.Settings to "Танзимот").forEach { (icon, label) -> Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = Color(0xFFFF7A45)); Spacer(Modifier.width(14.dp)); Text(label, Modifier.weight(1f), fontWeight = FontWeight.Medium); Icon(Icons.Default.ChevronRight, null, tint = Color(0xFFAAA49C)) } } }
    }
}

@Composable
private fun AdminScreen(tab: AdminTab) {
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF7F8FA)), contentPadding = PaddingValues(bottom = 30.dp)) { item { when (tab) { AdminTab.Dashboard -> DashboardScreen(); AdminTab.Orders -> OrdersScreen(); AdminTab.Tables -> TablesScreen(); AdminTab.Menu -> AdminMenuScreen() } } }
}

@Composable
private fun DashboardScreen() {
    TopBar("Панели админ", "Имрӯз: 23 ноябр") { Surface(shape = CircleShape, color = Color(0xFF23201C)) { Text("AM", Modifier.padding(11.dp), color = Color.White, fontWeight = FontWeight.Bold) } }
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(200.dp).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { StatCard("Даромад", "8 420", "сомонӣ", Icons.Default.Payments, Color(0xFFEFF8F4), Color(0xFF2F8F71)) }
        item { StatCard("Фармоишҳо", "47", "+12 имрӯз", Icons.Default.ReceiptLong, Color(0xFFFFF1E8), Color(0xFFFF7A45)) }
        item { StatCard("Мизҳои холӣ", "8", "аз 12", Icons.Default.TableBar, Color(0xFFEEF2FF), Color(0xFF586FCF)) }
        item { StatCard("Миёна", "179", "сомонӣ", Icons.Default.Analytics, Color(0xFFFFF7DB), Color(0xFFE8A817)) }
    }
    SectionTitle("Фармоишҳои нав")
    repeat(3) { index -> Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Surface(shape = CircleShape, color = Color(0xFFFFE5DA)) { Text("#${147-index}", Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = Color(0xFFFF7A45), fontSize = 12.sp) }; Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(listOf("Фирдавсӣ", "Шахзода", "Акбар")[index], fontWeight = FontWeight.Bold); Text(listOf("3 табақ • 142 сомонӣ", "2 табақ • 96 сомонӣ", "5 табақ • 218 сомонӣ")[index], fontSize = 12.sp, color = Color(0xFF858079)) }; AssistChip(onClick = {}, label = { Text(listOf("Нав", "Омода", "Роҳандозӣ")[index]) }) } } }
}

@Composable
private fun StatCard(title: String, value: String, detail: String, icon: ImageVector, background: Color, accent: Color) {
    Card(Modifier.fillMaxWidth().height(94.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = background)) { Row(Modifier.fillMaxSize().padding(14.dp), verticalAlignment = Alignment.Top) { Column(Modifier.weight(1f)) { Text(title, fontSize = 11.sp, color = Color(0xFF777269)); Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold); Text(detail, fontSize = 10.sp, color = accent) }; Icon(icon, null, tint = accent) } }
}

@Composable
private fun OrdersScreen() {
    TopBar("Фармоишҳо", "Ҳамаи фармоишҳои актив")
    listOf("Нав" to Color(0xFFFF7A45), "Омода" to Color(0xFFE2A82B), "Дар роҳ" to Color(0xFF586FCF), "Анҷом ёфта" to Color(0xFF2F8F71)).forEachIndexed { index, (status, color) -> Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Column(Modifier.padding(14.dp)) { Row { Text("Фармоиш #${210-index}", Modifier.weight(1f), fontWeight = FontWeight.Bold); Text(status, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp) }; Spacer(Modifier.height(8.dp)); Text(listOf("Пицца, бургер ×2", "Лосось, суп", "Паста карбонара ×2", "Фруктовый десерт")[index], color = Color(0xFF777269)); Spacer(Modifier.height(10.dp)); Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) { OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp)) { Text("Тафсилот") }; Spacer(Modifier.width(8.dp)); Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = color)) { Text("Навсозӣ") } } } } }
}

@Composable
private fun TablesScreen() {
    TopBar("Мизҳо", "12 миз • 8 холӣ")
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(480.dp).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { items((1..12).toList()) { n -> val free = n in listOf(1,2,4,6,8,9,11,12); Card(Modifier.aspectRatio(.9f), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = if (free) Color(0xFFEAF7F1) else Color(0xFFFFECE5))) { Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Icon(Icons.Default.TableRestaurant, null, tint = if (free) Color(0xFF2F8F71) else Color(0xFFFF7A45), modifier = Modifier.size(32.dp)); Text("Миз $n", fontWeight = FontWeight.Bold); Text(if (free) "Холӣ" else "Ишғол", fontSize = 11.sp, color = if (free) Color(0xFF2F8F71) else Color(0xFFC85B3C)) } } } }
}

@Composable
private fun AdminMenuScreen() {
    TopBar("Идоракунии меню")
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7A45))) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Add, null, tint = Color.White); Spacer(Modifier.width(10.dp)); Text("Иловаи табақи нав", color = Color.White, fontWeight = FontWeight.Bold) } }
    Spacer(Modifier.height(12.dp)); Palette.menu.forEach { food -> Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Text(food.emoji, fontSize = 28.sp); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(food.name, fontWeight = FontWeight.Bold); Text("${food.price.toInt()} сомонӣ", fontSize = 12.sp, color = Color(0xFF777069)) }; Switch(checked = true, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFFFF7A45))) } } }
}
