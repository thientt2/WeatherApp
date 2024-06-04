package com.example.weatherapp.ui.advancedscreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.DoubleArrow
import com.example.weatherapp.ui.theme.Grey40
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.weatherapp.ui.theme.Green40
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.ui.theme.Grey80
import com.example.weatherapp.viewmodal.AdvancedViewModel
import com.example.weatherapp.modal.NewsItem
import com.example.weatherapp.ui.theme.LightBlue

@Composable
fun TrendingNewsHeader(onViewAllClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Tin Nổi Bật",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Grey80,
        )

        Spacer(modifier = Modifier.weight(10f))

        Text(
            text = "Xem tất cả",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Green40,
            modifier = Modifier.clickable { onViewAllClicked() }
        )

        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            imageVector = Icons.Default.DoubleArrow,
            contentDescription = "View All",
            tint = Green40,
            modifier = Modifier
                .size(24.dp)
                .clickable { onViewAllClicked() }

        )
    }
}

@Composable
fun CategoryList (selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Phổ Biến", "Thể Thao", "Chính Trị", "Công nghệ", "Sức Khỏe", "Kinh Doanh", "Khoa Học", "Giải Trí", "Môi Trường", "Thức Ăn")

    LazyRow( modifier = Modifier
        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = onCategorySelected
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    val backgroundColor = if (isSelected) Green40 else Grey40
    val color = if (isSelected) Color.White else Color.Black
    val borderModifier = if (!isSelected) {
        Modifier.border(width = 2.dp, color = Green40, shape = RoundedCornerShape(20.dp))
    } else {
        Modifier
    }
    BasicText(
        text = category.replaceFirstChar { if(it.isLowerCase()) it.titlecase() else it.toString() },
        modifier = Modifier
            .padding(start = 8.dp, end = 2.dp, top = 8.dp, bottom = 8.dp)
            .then(borderModifier)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick(category) }
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
        style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    )
}

@Composable
fun TrendingNewsList(newsItems: List<NewsItem>,  onNewsItemSelected: (NewsItem) -> Unit) {
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        items(newsItems) { newsItem ->
            TrendingNewsItemBox(newsItem, onNewsItemSelected)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrendingNewsItemBox(newsItem: NewsItem, onItemClick: (NewsItem) -> Unit) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
//    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            .width(300.dp)
            .heightIn(max = 350.dp, min = 350.dp)
            .shadow(
                elevation = 7.dp,  // Adjust this value to increase/decrease shadow size
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),  // Lower alpha for less blur
                spotColor = Color.Black.copy(alpha = 0.4f)     // Lower alpha for less blur
            )
            .background(Color.White)
            .clickable { onItemClick(newsItem) }

    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        ) {
            Image(
                painter = painter,
                contentDescription = newsItem.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = newsItem.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(textAlign = TextAlign.Justify),
            lineHeight = 30.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        ClickableText(
            text = AnnotatedString("source"),
            style = TextStyle(
                fontSize = 25.sp,
                color = Green40,
                fontWeight = FontWeight.W600,
            ),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            onClick = {
                onItemClick(newsItem) // Gọi hàm callback khi nhấn vào
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            text = newsItem.pubDate,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun WorldNewsHeader() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Tin Quốc Tế",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Grey80,
        )
    }
}

@Composable
fun WorldNewsList(newsItems: List<NewsItem>, onItemClick: (NewsItem) -> Unit) {
    var isLoading by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 78.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading && newsItems.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
//                    .padding(top = 30.dp)
                , // Adjust padding as needed
                color = Green40,
                strokeWidth = 4.dp
            )
        } else {
            isLoading = false
            newsItems.forEach { newsItem ->
                WorldNewsItemBox(newsItem, onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WorldNewsItemBox(newsItem: NewsItem, onItemClick: (NewsItem) -> Unit) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
            .height(200.dp)
            .shadow(
                elevation = 7.dp,  // Adjust this value to increase/decrease shadow size
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),  // Lower alpha for less blur
                spotColor = Color.Black.copy(alpha = 0.4f)     // Lower alpha for less blur
            )
            .background(Color.White)
            .clickable { onItemClick(newsItem) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .height(160.dp)
                .weight(1.3f)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painter,
                contentDescription = newsItem.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .padding(end = 30.dp)
                .weight(1.2f),
            text = newsItem.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NewsDetail(newsItem: NewsItem, onBackClicked: () -> Unit) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quay lại",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Grey80,
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() },
                        modifier = Modifier
                            .padding(start = 24.dp, bottom = 2.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "View All",
                            tint = Grey80,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Create and launch share intent
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, newsItem.link)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        },
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Grey80,
                        )
                    }
                },
                backgroundColor = Grey40, // Sử dụng màu của surface cho top bar
                elevation = 0.dp, // Bỏ phần border của top bar
                modifier = Modifier
                    .fillMaxWidth() // Ensure it takes the full width
                    .padding(top = 16.dp, bottom = 8.dp)
            )
        },
        backgroundColor = Grey40,
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .height(230.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = newsItem.description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        style = TextStyle(textAlign = TextAlign.Justify),
                        lineHeight = 40.sp,
                    )

                    Text(
                        text = "Source",
                        style = TextStyle(
                            fontSize = 25.sp,
                            color = Green40,
                            fontWeight = FontWeight.W600,
                        ),
                        modifier = Modifier.padding(top = 24.dp),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.pubDate,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.description,
                        fontSize = 20.sp,
                        style = TextStyle(textAlign = TextAlign.Justify),
                        lineHeight = 30.sp,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        ClickableText(
                            text = AnnotatedString("Đọc thêm"),
                            style = TextStyle(
                                fontSize = 23.sp,
                                color = LightBlue,
                                fontWeight = FontWeight.W600,
                            ),
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                                context.startActivity(intent)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    )
}

@Composable
fun TrendingNewsListAll(newsItems: List<NewsItem>, onBackClicked: () -> Unit, onItemClick: (NewsItem) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quay lại",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Grey80,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() },
                        modifier = Modifier
                            .padding(start = 24.dp, bottom = 2.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Grey80,
                        )
                    }
                },
                backgroundColor = Grey40,
                elevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )
        },
        backgroundColor = Grey40,
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                items(newsItems) { newsItem ->
                    WorldNewsItemBox(newsItem, onItemClick)
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAdvanced() {
    WeatherAppTheme {
        Advanced()
    }
}

@Composable
fun Advanced(viewModel: AdvancedViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var selectedCategory by remember { mutableStateOf("top") }
    val (selectedNewsItem, setSelectedNewsItem) = remember { mutableStateOf<NewsItem?>(null) }
    val (viewAll, setViewAll) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllNews()
    }

    DisposableEffect(Unit) {
        onDispose {
            println("cancel")
            viewModel.cancelJob()
        }
    }
    
    LaunchedEffect(selectedCategory) {
        viewModel.filterNewsByCategory(selectedCategory)
    }

    val newsItem by viewModel.newsItems.collectAsState()
    val allNews by viewModel.allItems.collectAsState()

    if (viewAll) {
        TrendingNewsListAll(
            newsItems = allNews,
            onBackClicked = { setViewAll(false) },
            onItemClick = { newItem -> setSelectedNewsItem(newItem) }
        )
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .background(Grey40)
                        .fillMaxSize()
                ) {
                    Column {
                        var isLoading by remember { mutableStateOf(true) }
                        TrendingNewsHeader { setViewAll(true) }
                        CategoryList(selectedCategory) { category ->
                            selectedCategory = category
                        }
                        if (isLoading && newsItem.isEmpty()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterHorizontally),
                                color = Green40,
                                strokeWidth = 4.dp
                            )
                        } else {
                            isLoading = false
                            TrendingNewsList(newsItem) { newsItem ->
                                setSelectedNewsItem(newsItem)
                            }
                        }
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .background(Grey40)
                        .fillMaxSize()
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.fetchNewsWorld()
                    }

                    val newsWorldItem by viewModel.newsWorldItems.collectAsState()
                    WorldNewsHeader()
                    WorldNewsList(newsWorldItem) { newsItem ->
                        setSelectedNewsItem(newsItem)
                    }
                }
            }
        }
    }


    selectedNewsItem?.let { newsItem ->
        NewsDetail(newsItem) {
            setSelectedNewsItem(null) // Đặt lại tin tức được chọn về null khi nhấn nút Back
        }
    }
}