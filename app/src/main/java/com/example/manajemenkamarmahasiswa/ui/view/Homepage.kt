package com.example.manajemenkamarmahasiswa.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manajemenkamarmahasiswa.R

@Preview(showBackground = true)
@Composable
fun Homepage(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        HeaderSection()
        BodySection(onItemClick = onItemClick)
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 48.dp))
            .background(color = colorResource(R.color.primary))
            .padding(bottom = 32.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 24.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.iconkapasitas),
                    contentDescription = "Home Icon",
                    modifier = Modifier.padding(8.dp)
                        .size(52.dp)
                )
                Spacer(Modifier.padding(3.dp))
                Text(
                    text = "Welcome",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "to PapaPas's Mobile App",
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "Papa Pengelola Asrama, Kelola Asrama mu Disini!",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Box(
            Modifier.align(Alignment.CenterEnd)
                .padding(24.dp)
                .padding(top = 12.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.iconkapasitas),
                contentDescription = "Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun BodySection(
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pilih Fitur untuk Dikelola",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
        )

        ManageBox(
            title = "Manajemen Asrama",
            description = "Pantau dan kelola semua data asrama Anda dengan mudah.",
            backgroundColor = Color(0xFF1DDBAF),
            iconResource = R.drawable.iconasrama,
            onClick = { onItemClick("Bangunan") }
        )

        ManageBox(
            title = "Manajemen Kamar",
            description = "Tambah, edit, atau hapus data kamar asrama Anda.",
            backgroundColor = Color(0xFF62D2A2),
            iconResource = R.drawable.iconkamar,
            onClick = { onItemClick("Kamar") }
        )

        ManageBox(
            title = "Manajemen Mahasiswa",
            description = "Atur data penghuni asrama dengan lebih terorganisir.",
            backgroundColor = Color(0xFF62B6CB),
            iconResource = R.drawable.iconmahasiswa,
            onClick = { onItemClick("Mahasiswa") }
        )

        ManageBox(
            title = "Manajemen Pembayaran",
            description = "Pantau dan kelola pembayaran sewa asrama.",
            backgroundColor = Color(0xFF4D96FF),
            iconResource = R.drawable.iconpembayaran,
            onClick = { onItemClick("Pembayaran Sewa") }
        )

        Spacer(modifier = Modifier.height(2.dp))


    }
}

@Composable
fun ManageBox(
    title: String,
    description: String,
    backgroundColor: Color,
    iconResource: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = "$title Icon",
                modifier = Modifier
                    .size(48.dp)
            )
        }
    }
}
