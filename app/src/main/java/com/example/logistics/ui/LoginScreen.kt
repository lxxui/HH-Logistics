package com.example.logistics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // 👈 เพิ่มอิมพอร์ต sp แก้ไขอาการฟ้องแดงที่ตัวหนังสือ

// ========================================================
// 1. โครงสร้างการสลับหน้าจอ (MainApp) ตัวควบคุมหลัก
// ========================================================
@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf("login") }
    // 🎯 ประกาศตัวแปรเก็บสถานะสาขาที่เลือกไว้ตรงนี้ เพื่อแก้ปัญหา Unresolved reference
    var selectedBranch by remember { mutableStateOf<Branch?>(null) }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                onLoginSuccess = { currentScreen = "dashboard" }
            )
        }
        "dashboard" -> {
            DashboardScreen(
                onLogout = { currentScreen = "login" },
                onNavigateToPicking = {
                    // 🚀 เมื่อกดปุ่มจัดสินค้า ให้สั่งสลับมาหน้าเลือกสาขา
                    currentScreen = "branch_select"
                }
            )
        }
        "branch_select" -> {
            // เรียกใช้งานหน้าเลือกสาขา
            BranchSelectScreen(
                onBranchSelected = { branch ->
                    selectedBranch = branch
                    // อนาคตส่งไปหน้าสแกนบาร์โค้ดจัดสินค้าในขั้นตอนต่อไป
                    // currentScreen = "picking_process"
                }
            )
        }
    }
}

// ========================================================
// 2. ฟังก์ชัน LoginScreen สไตล์ Enterprise Clean
// ========================================================
// ========================================================
// 2. ฟังก์ชัน LoginScreen สไตล์ Enterprise Clean (แก้ไข Username / Password)
// ========================================================
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    // 🔐 เปลี่ยนกลับมาเป็น username และ password ตามโครงสร้างระบบเดิมของคุณฝ้าย
    var username by remember { mutableStateOf("723582") }
    var password by remember { mutableStateOf("3582") }
    var errorMessage by remember { mutableStateOf("") }

    val blueGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueGradient),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .border(width = 1.dp, color = Color(0xFFDEE2E6), shape = RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ไอคอนกล่องสินค้าด้านบน
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFE7F0FF), shape = RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📦",
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "เข้าสู่ระบบจัดสินค้า",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212529)
                    )
                )
                Text(
                    text = "ระบบบริหารจัดการคลังสินค้า",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 📝 ช่องกรอก Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2A5298),
                        focusedLabelColor = Color(0xFF2A5298)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 📝 ช่องกรอก Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2A5298),
                        focusedLabelColor = Color(0xFF2A5298)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // กล่องแจ้งเตือน Error
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8D7DA), shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, color = Color(0xFFF5C2C7), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage,
                                color = Color(0xFF842029),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ปุ่มเข้าสู่ระบบ ตรวจสอบค่า username และ password ตรงๆ
                Button(
                    onClick = {
                        if (username == "723582" && password == "3582") {
                            errorMessage = ""
                            onLoginSuccess()
                        } else {
                            errorMessage = "Username หรือ Password ไม่ถูกต้อง"
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A5298)),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "เข้าสู่ระบบ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}