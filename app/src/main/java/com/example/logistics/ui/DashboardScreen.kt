package com.example.logistics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onNavigateToPicking: () -> Unit
) {
    Scaffold(
        topBar = {
            // แถบด้านบนสไตล์ Bootstrap Navbar (สีน้ำเงินเข้ม) ปลอดภัยไร้ค่า Elevation
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ระบบคลังสินค้า",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ออกจากระบบ 🚪",
                        color = Color(0xFFF8D7DA),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onLogout() }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // พื้นหลังสีเทาอ่อน สไตล์ Bootstrap Background
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            // ส่วนต้อนรับพนักงาน
            Text(
                text = "ยินดีต้อนรับพนักงานจัดสินค้า",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )
            Text(
                text = "กรุณาเลือกรายการปฏิบัติงานด้านล่าง",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🛒 การ์ด "จัดสินค้า" สไตล์ Bootstrap Border
            // ถอดเงาออกทั้งหมด แล้วใช้เส้นขอบสีเทาอ่อน (0xFFDEE2E6) แทน ปลอดภัยจาก Error 100%
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .border(width = 1.dp, color = Color(0xFFDEE2E6), shape = RoundedCornerShape(16.dp))
                    .clickable { onNavigateToPicking() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // วงกลมไอคอนหน้ารถเข็น
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFE7F0FF), shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛒", fontSize = 26.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // ข้อความอธิบายเมนู (แก้ไขจุดดึงสิทธิ์น้ำหนัก weight ให้เป็น Float 1f)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "จัดสินค้า",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3C72)
                        )
                        Text(
                            text = "เตรียมและจัดตะกร้าสินค้าตามออเดอร์",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    // ลูกศรชี้ไปข้างหน้า
                    Text(
                        text = "▶",
                        color = Color(0xFF2A5298),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}