package com.example.logistics.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class PickingItem(
    val unitCode: String,
    val targetStack: Int,
    val targetTray: Int,
    val targetPiece: Int,
    var pickedStack: Int,  // ปรับเป็น var เพื่อให้เปลี่ยนค่าภายใน Screen ได้
    var pickedTray: Int,   // ปรับเป็น var เพื่อให้เปลี่ยนค่าภายใน Screen ได้
    var pickedPiece: Int,  // ปรับเป็น var เพื่อให้เปลี่ยนค่าภายใน Screen ได้
    var status: String     // ปรับเป็น var เพื่อให้อัปเดตสถานะได้
)

@Composable
fun PickingExecutionScreen(
    jobId: String,
    zoneId: String,
    onFinishJob: () -> Unit
) {
    val darkGreen = Color(0xFF0F5132)
    val lightGreenBg = Color(0xFFD1E7DD)
    val successGreen = Color(0xFF198754)
    val grayBackground = Color(0xFFF8F9FA)
    val borderGray = Color(0xFFDEE2E6)

    // ข้อมูลจำลองรายการจัดสินค้า
    var itemsList by remember { mutableStateOf(listOf(
        PickingItem("110301", 2, 4, 0, 2, 4, 0, "DONE"),
        PickingItem("110302", 3, 0, 2, 3, 0, 2, "DONE"),
        PickingItem("110303", 1, 5, 0, 1, 5, 0, "DONE"),
        PickingItem("110304", 3, 2, 1, 1, 0, 0, "WAITING"),
        PickingItem("110305", 5, 1, 0, 0, 0, 0, "WAITING"),
        PickingItem("110306", 0, 8, 3, 0, 0, 0, "WAITING")
    )) }

    // คีย์หลัก: เปลี่ยนจากดึงตัวแรกที่รออยู่ มาเป็นให้ระบบจำว่าตอนนี้เลือกหน่วยไหนอยู่
    var selectedUnitCode by remember {
        mutableStateOf(itemsList.firstOrNull { it.status == "WAITING" }?.unitCode ?: "")
    }

    // ค้นหา Object สินค้าปัจจุบันตามรหัสที่เลือกอยู่
    val currentTarget = itemsList.find { it.unitCode == selectedUnitCode }
    val isAllDone = itemsList.all { it.status == "DONE" }

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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "ระบบจัดสินค้า (Handheld)", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        Text(text = "ใบงาน: $jobId", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Surface(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                        Text(text = "ONLINE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
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
                .padding(16.dp)
        ) {

            // 🎯 ส่วนที่ 1: การ์ดสินค้าตัวที่เลือกปัจจุบัน (ไม่ล็อกโฟลว์ สลับไปมาได้)
            if (!isAllDone && currentTarget != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, color = borderGray, shape = RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(text = "🎯 หน่วยจัดที่เลือกปฏิบัติงาน", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Text(text = currentTarget.unitCode, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = darkGreen)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 📋 ฝั่งเป้าหมายจากใบงาน
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(text = "เป้าหมายต้องจัด", fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${currentTarget.targetStack} ตั้ง\n${currentTarget.targetTray} ถาด\n${currentTarget.targetPiece} ชิ้น",
                                    fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF343A40), textAlign = TextAlign.Center
                                )
                            }

                            Box(modifier = Modifier.width(1.dp).height(50.dp).background(borderGray))

                            // ✅ ฝั่งจัดจริงปัจจุบัน (ขึ้นตามที่กด/สแกนจริง)
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(text = "จัดได้ปัจจุบัน", fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${currentTarget.pickedStack} ตั้ง\n${currentTarget.pickedTray} ถาด\n${currentTarget.pickedPiece} ชิ้น",
                                    fontSize = 15.sp, fontWeight = FontWeight.Bold, color = successGreen, textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 🛠️ ปุ่มสำหรับเลือกสินค้า/กรอกจำนวนด้วยมือ (Manual Input Action) - คลีนและไม่มี Error เรื่องไอคอนแล้ว
                        Text(text = "👇 เลือกจัดการสินค้าโดยไม่ต้องสแกน (Manual)", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    itemsList = itemsList.map {
                                        if (it.unitCode == currentTarget.unitCode) {
                                            val newStack = it.pickedStack + 1
                                            it.copy(
                                                pickedStack = newStack,
                                                status = if (newStack >= it.targetStack && it.pickedTray >= it.targetTray && it.pickedPiece >= it.targetPiece) "DONE" else "WAITING"
                                            )
                                        } else it
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("+ ตั้ง", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    itemsList = itemsList.map {
                                        if (it.unitCode == currentTarget.unitCode) {
                                            val newTray = it.pickedTray + 1
                                            it.copy(
                                                pickedTray = newTray,
                                                status = if (it.pickedStack >= it.targetStack && newTray >= it.targetTray && it.pickedPiece >= it.targetPiece) "DONE" else "WAITING"
                                            )
                                        } else it
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("+ ถาด", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    itemsList = itemsList.map {
                                        if (it.unitCode == currentTarget.unitCode) {
                                            val newPiece = it.pickedPiece + 1
                                            it.copy(
                                                pickedPiece = newPiece,
                                                status = if (it.pickedStack >= it.targetStack && it.pickedTray >= it.targetTray && newPiece >= it.targetPiece) "DONE" else "WAITING"
                                            )
                                        } else it
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("+ ชิ้น", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else if (isAllDone) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = lightGreenBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        Text(text = "🎉 จัดสินค้าเสร็จสิ้นครบถ้วนทุกรายการ!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = darkGreen)
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onFinishJob,
                            colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "ส่งงานและปิดใบจัดสินค้า", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 📊 ส่วนที่ 2: ตารางแสดงรายการทั้งหมด (แตะเลือกสินค้าจากแถวบนตารางได้เลย)
            Text(
                text = "📋 รายการจัดสินค้า โซน $zoneId (แตะที่แถวเพื่อเลือกหน่วยจัดได้)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE9ECEF), shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "หน่วยจัด", modifier = Modifier.weight(1.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF495057))
                Text(text = "ตั้ง (เป้า/ทำ)", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF495057))
                Text(text = "ถาด (เป้า/ทำ)", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF495057))
                Text(text = "ชิ้น (เป้า/ทำ)", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF495057))
                Text(text = "สถานะ", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.End, color = Color(0xFF495057))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .border(1.dp, borderGray, shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            ) {
                items(itemsList) { item ->
                    val isCurrent = selectedUnitCode == item.unitCode

                    val rowBackground = when {
                        isCurrent -> Color(0xFFE8F5E9) // ไฮไลต์แถวที่กำลังเลือกทำงานอยู่
                        item.status == "DONE" -> Color(0xFFF8F9FA)
                        else -> Color.White
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowBackground)
                            .clickable {
                                selectedUnitCode = item.unitCode
                            }
                            .padding(vertical = 14.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.unitCode,
                            modifier = Modifier.weight(1.3f),
                            fontSize = 14.sp,
                            fontWeight = if(isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if(isCurrent) darkGreen else Color(0xFF212529)
                        )

                        Text(
                            text = "${item.targetStack}/${item.pickedStack}",
                            modifier = Modifier.weight(1.2f), fontSize = 14.sp, textAlign = TextAlign.Center,
                            color = if(item.targetStack == item.pickedStack && item.targetStack > 0) successGreen else Color(0xFF495057),
                            fontWeight = if(isCurrent) FontWeight.Bold else FontWeight.Normal
                        )

                        Text(
                            text = "${item.targetTray}/${item.pickedTray}",
                            modifier = Modifier.weight(1.2f), fontSize = 14.sp, textAlign = TextAlign.Center,
                            color = if(item.targetTray == item.pickedTray && item.targetTray > 0) successGreen else Color(0xFF495057),
                            fontWeight = if(isCurrent) FontWeight.Bold else FontWeight.Normal
                        )

                        Text(
                            text = "${item.targetPiece}/${item.pickedPiece}",
                            modifier = Modifier.weight(1.2f), fontSize = 14.sp, textAlign = TextAlign.Center,
                            color = if(item.targetPiece == item.pickedPiece && item.targetPiece > 0) successGreen else Color(0xFF495057),
                            fontWeight = if(isCurrent) FontWeight.Bold else FontWeight.Normal
                        )

                        Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.CenterEnd) {
                            Text(text = if (item.status == "DONE") "✅" else "⏳", fontSize = 14.sp)
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F3F5))
                }
            }
        }
    }
}