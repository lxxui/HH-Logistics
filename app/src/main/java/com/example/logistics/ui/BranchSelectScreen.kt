package com.example.logistics.ui

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// โมเดลข้อมูลสาขา
data class Branch(
    val id: String,
    val name: String
)

// รายชื่อสาขา (ทดสอบไว้ก่อน)
val sampleBranches = listOf(
    Branch("1103", "สาขาพระยาสุเรนทร์"),
    Branch("1101", "สาขาศรีนครินทร์"),
)

@Composable
fun BranchSelectScreen(
    onBranchSelected: (Branch) -> Unit
) {
    // 🔍 ตัวแปรสำหรับเก็บค่าคำค้นหาที่ผู้ใช้กรอก
    var searchQuery by remember { mutableStateOf("") }

    // ⚡ ฟังก์ชันกรองข้อมูลสาขาตามคำค้นหา (รองรับทั้งการพิมพ์ชื่อสาขา หรือ รหัสสาขา)
    val filteredBranches = remember(searchQuery) {
        sampleBranches.filter { branch ->
            branch.name.contains(searchQuery, ignoreCase = true) ||
                    branch.id.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            // แถบด้านบนสไตล์คุมโทนน้ำเงินเข้มระดับ Enterprise
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E3C72))
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "เลือกสถานที่ปฏิบัติงาน",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // พื้นหลังสีเทาอ่อน สไตล์ Bootstrap
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "เลือกสาขาที่ต้องการจัดสินค้า",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )
            Text(
                text = "ระบบจะดึงข้อมูลออเดอร์ตามสาขาที่คุณเลือก",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ⌨️ ช่องกรอกคำค้นหาสาขา (Search Bar) สไตล์คลีนเรียบง่าย
            // ⌨️ ช่องกรอกคำค้นหาสาขา (Search Bar) ฉบับแก้ไขพารามิเตอร์สี
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("พิมพ์ชื่อสาขา หรือ รหัสสาขา เพื่อค้นหา...") },
                leadingIcon = { Text("🔍", modifier = Modifier.padding(start = 8.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Text(
                            text = "✖️",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2A5298),  // สีขอบตอนกดเลือก
                    unfocusedBorderColor = Color(0xFFDEE2E6) // สีขอบตอนปกติ
                    // 🛠️ ลบ containerColor ออกเพื่อแก้ปัญหาฟ้องแดงเวอร์ชัน Material 3
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ส่วนแสดงรายการสาขาที่ผ่านการกรองแล้ว
            if (filteredBranches.isEmpty()) {
                // แสดงเมื่อค้นหาแล้วไม่เจอข้อมูล
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "❌ ไม่พบสาขาที่ค้นหา",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredBranches) { branch ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .border(width = 1.dp, color = Color(0xFFDEE2E6), shape = RoundedCornerShape(12.dp))
                                .clickable { onBranchSelected(branch) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // วงกลมไอคอนปักหมุด 📍
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color(0xFFE7F0FF), shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "📍", fontSize = 20.sp)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = branch.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF212529)
                                    )
                                    Text(
                                        text = "รหัสสาขา: ${branch.id}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }

                                // ลูกศรชี้ไปข้างหน้า
                                Text(
                                    text = "▶",
                                    color = Color(0xFF2A5298),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}