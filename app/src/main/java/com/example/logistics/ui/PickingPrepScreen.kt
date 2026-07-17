package com.example.logistics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 📊 เพิ่มฟิลด์ branchName เพื่อเช็คความถูกต้องของสินค้าในแต่ละสาขา
data class BranchProductTarget(
    val branchName: String,
    val jobId: String,
    val targetStack: Int,
    val targetTray: Int,
    val targetPiece: Int
)

@Composable
fun PickingPrepScreen(
    branchName: String,
    onStartPickingWithDetails: (jobId: String, zoneId: String, locationCode: String, stack: Int, tray: Int, piece: Int) -> Unit,
    onBack: () -> Unit
) {
    // 🎨 Theme สีเขียวเข้ม Enterprise มินิมอล
    val darkGreen = Color(0xFF0F5132)
    val successGreen = Color(0xFF198754)
    val grayBackground = Color(0xFFF8F9FA)
    val borderGray = Color(0xFFDEE2E6)

    var jobId by remember { mutableStateOf("") }
    var zoneId by remember { mutableStateOf("") }
    var locationCode by remember { mutableStateOf("") }

    // 📊 State สำหรับเก็บข้อมูล ตั้ง/ถาด/ชิ้น ที่ดึงมาแบบ Auto
    var stackCount by remember { mutableStateOf(0) }
    var trayCount by remember { mutableStateOf(0) }
    var pieceCount by remember { mutableStateOf(0) }
    var isProductFound by remember { mutableStateOf(false) }

    // 🔍 จำลองฐานข้อมูลสินค้าที่แยกตามสาขาชัดเจน (คุณฝ้ายเอาไปใช้ Map กับ API หรือ SQL ต่อได้เลย)
    val mockBranchProducts = remember {
        listOf(
            // ตัวอย่างข้อมูลสำหรับกรณีชื่อสาขาเป็นข้อความทดสอบ หรือชื่อสาขาจริงในระบบ
            BranchProductTarget("สาขาหลัก", "JOB69001", 3, 4, 0),
            BranchProductTarget("สาขาหลัก", "JOB69002", 5, 0, 12),
            BranchProductTarget("สำนักงานใหญ่", "JOB69001", 10, 2, 5),
            BranchProductTarget(branchName, "JOB-TEST", 1, 2, 3) // ดักจับตามชื่อสาขาที่ส่งเข้ามาจริง
        )
    }

    // ⚡ ฟังก์ชันตรวจจับและดึงข้อมูล Auto-Fetch โดยกรองจาก "สาขาที่เลือก" และ "รหัสใบจัด" ร่วมกัน
    LaunchedEffect(jobId, branchName) {
        val foundProduct = mockBranchProducts.find {
            it.branchName.trim() == branchName.trim() &&
                    it.jobId == jobId.trim().uppercase()
        }

        if (foundProduct != null) {
            stackCount = foundProduct.targetStack
            trayCount = foundProduct.targetTray
            pieceCount = foundProduct.targetPiece
            isProductFound = true
        } else {
            stackCount = 0
            trayCount = 0
            pieceCount = 0
            isProductFound = false
        }
    }

    val isFormValid = jobId.isNotEmpty() &&
            zoneId.isNotEmpty() &&
            locationCode.isNotEmpty() &&
            isProductFound

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkGreen)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔙",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { onBack() },
                        color = Color.White
                    )
                    Column {
                        Text(text = "ระบบจัดสินค้า (Dispatching Prep)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = branchName, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(grayBackground)
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "เตรียมความพร้อมก่อนจัดสินค้า",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212529)
                )
                Text(
                    text = "ข้อมูลเป้าหมายจะอ้างอิงตามสาขา $branchName และรหัสใบจัดที่ระบุ",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(text = "รหัสใบจัดสินค้า (คีย์ทดสอบ: JOB-TEST)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF495057))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = jobId,
                            onValueChange = { jobId = it },
                            placeholder = { Text("สแกนบาร์โค้ด...") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkGreen, unfocusedBorderColor = borderGray),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(0.8f)) {
                        Text(text = "รหัสชั้นวิ่ง / ล็อคจัด", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF495057))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = zoneId,
                            onValueChange = { zoneId = it },
                            placeholder = { Text("เช่น โซน A") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkGreen, unfocusedBorderColor = borderGray),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "รหัสชี้บ่งประจำแท่น (ระบุ A-Z ตามด้วยตัวเลข เช่น A 002)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF495057))
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = locationCode,
                    onValueChange = { input ->
                        if (input.length <= 6) {
                            locationCode = input.uppercase()
                        }
                    },
                    placeholder = { Text("พิมพ์ระบุ เช่น B 005") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkGreen, unfocusedBorderColor = borderGray),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = borderGray)
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "📋 จำนวนเป้าหมายจากสาขา (Auto-Fetched)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = darkGreen)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            Text(text = "ตั้ง", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "$stackCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = if(isProductFound) darkGreen else Color.LightGray)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            Text(text = "ถาด", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "$trayCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = if(isProductFound) darkGreen else Color.LightGray)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            Text(text = "ชิ้น", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "$pieceCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = if(isProductFound) darkGreen else Color.LightGray)
                        }
                    }
                }

                if (jobId.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isProductFound) "✅ พบข้อมูลสินค้าของสาขา $branchName เรียบร้อย" else "❌ ไม่พบรหัสใบงานนี้ในข้อมูลของสาขา $branchName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isProductFound) successGreen else Color.Red,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                onClick = {
                    if (isFormValid) {
                        onStartPickingWithDetails(jobId, zoneId, locationCode, stackCount, trayCount, pieceCount)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = successGreen,
                    disabledContainerColor = Color(0xFFCED4DA)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "🚀 ยืนยันพิกัดและเริ่มจัดสินค้า", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}