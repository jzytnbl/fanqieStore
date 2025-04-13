<script setup lang="ts">
import {ref, computed, onMounted, reactive} from 'vue';
import {ElMessage, UploadProps} from 'element-plus';
import { Search, BellFilled, ShoppingCart, Plus, ShoppingCartFull} from '@element-plus/icons-vue'
import {addProduct, getProductInfo, getSpecificProduct} from "@/api/product";
import { getAdvertisement } from "@/api/advertisement";
import { useRouter } from 'vue-router';
import {getCart} from "@/api/cart";

// 在页面加载时获取所有书籍、广告和购物车
onMounted(() => {
  fetchBookList();
  fetchAdvertisement();
  fetchCart();
})

const router = useRouter();

// 购物车提交窗口是否可见
const centerDialogVisible1 = ref(false);
// 创建商品窗口是否可见
const centerDialogVisible2 = ref(false);

// 获得商品模块
interface ProductVO {
  id: number,
  title: string,
  price: number,
  rate: number,
  description: string,
  cover: string,
  detail: string,
  specifications: Specification[];
  stockpile: Stockpile;
}

interface Specification {
  id: number,
  item: string,
  value: string,
  product_id: number;
}
interface Stockpile {
  id: number;
  product_id: number;
  amount: number;
  frozen: number;
}

// 已有的bookList
const bookList = ref<ProductVO[]>([]);

// 创建的book信息
interface CreateProductVO {
  title: string,
  price: number | null,
  rate: number,
  description: string,
  cover: string,
  detail: string,
  specifications: Specifications[];
}

interface Specifications {
  item: string,
  value: string,
}

const author = ref<string>('')
const subtitle = ref<string>('')
const ISBN = ref<string>('')
const bindingAndLayout = ref<string>('')// 装帧
const pages = ref<string>('')
const publishingHouse = ref<string>('')
const publicationDate = ref<string>('')

const bookInfo = reactive<CreateProductVO>({
  title: '',
  price: null,
  rate: 0,
  description: '',
  cover: '',
  detail: '',
  specifications: [],
})
// 添加规格
const addSpecification = (Item: string, Value: string) => {
  bookInfo.specifications.push({
    item: Item,
    value: Value,
  });
}

// 广告模块
interface Advertisement {
  title: string,
  content: string,
  imageUrl: string,
  productId: string,
}

// const advertisementList = ref<Advertisement[]>([]);

// 购物车的书籍信息
interface CartItems {
  productId: string,
  title: string,
  price: number,
  description: string,
  cover: string,
  detail: string,
  quantity: number,
}

interface CartInfo {
  items: CartItems[],
  total: number,
  totalAmount: number,
}

// const cartData = ref<CartInfo>([])

function fetchBookList() {
  getProductInfo()
      .then((res) => {
        console.log("res:", res);
        console.log("res.data:", res.data);
        bookList.value = res.data.data;
      })
      .catch((error) => {
        console.error("书籍列表获取失败！", error);
      });
}

function fetchAdvertisement() {
  try {
    getAdvertisement().then((res) => {
      advertisementList.value = res.data;
    });
  }catch(error){
    ElMessage.error('广告获取失败，请稍后再试！');
  }
}

// 获取购物车（内涵商品信息、分别购买数量、总数目和总价）
function fetchCart() {
  try {
    getCart().then((res) => {
      cartData.value = res.data;
    })
  } catch (error) {
    ElMessage.error('获取购物车失败！');
  }
}

// 以下为测试数据
// const bookList = ref<ProductVO[]>([
//   {
//     id: '1111',
//     title: 'books111111111111111111',
//     price: 11111111111111,
//     rate: 3,
//     description: '商品描述',
//     cover: 'https://lab-blue-whale.oss-cn-nanjing.aliyuncs.com/%E6%B2%99%E6%BC%A0.jpg',
//     detail: '商品详细说明',
//     specifications: [
//       {
//         id: 'spec1',
//         item: '作者',
//         value: '周志明',
//         productId: '1111',
//       },
//       {
//         id: 'spec2',
//         item: '副标题',
//         value: 'JVM高级特性与最佳实践',
//         productId: '1111',
//       },
//     ],
//   },
// ]);
const advertisementList = ref([
  {
    title: 'books1111111111111111111111111111',
    content: 'stringdfgndkslpakdlavjfkdqaslcjnvdfskazxmcnv fhdjskazxmcjnvd 33333333333333333333333333333333333333333333333333333333333333333333333333333333',
    imageUrl: 'https://lab-blue-whale.oss-cn-nanjing.aliyuncs.com/%E6%B2%99%E6%BC%A0.jpg',
    productId: 1,
  },
  {
    title: 'books1111111111111111111111111111',
    content: 'stringdfgndkslpakdlavjfkdqaslcjnvdfskazxmcnv fhdjskazxmcjnvd 33333333333333333333333333333333333333333333333333333333333333333333333333333333',
    imageUrl: 'https://lab-blue-whale.oss-cn-nanjing.aliyuncs.com/%E6%B2%99%E6%BC%A0.jpg',
    productId: 1,
  },
  {
    title: 'books1111111111111111111111111111',
    content: 'stringdfgndkslpakdlavjfkdqaslcjnvdfskazxmcnv fhdjskazxmcjnvd 33333333333333333333333333333333333333333333333333333333333333333333333333333333',
    imageUrl: 'https://lab-blue-whale.oss-cn-nanjing.aliyuncs.com/%E6%B2%99%E6%BC%A0.jpg',
    productId: 1,
  },
  {
    title: 'books1111111111111111111111111111',
    content: 'stringdfgndkslpakdlavjfkdqaslcjnvdfskazxmcnv fhdjskazxmcjnvd 33333333333333333333333333333333333333333333333333333333333333333333333333333333',
    imageUrl: 'https://lab-blue-whale.oss-cn-nanjing.aliyuncs.com/%E6%B2%99%E6%BC%A0.jpg',
    productId: 1,
  }
])
// 以下是测试数据
const cartData = ref<CartInfo>({
  items: [
    {
      productId: '1',
      title: '雷锋的故事',
      price: 111,
      description: '描述1',
      cover: '封面1',
      detail: '详情1',
      quantity: 1,
    },
    {
      productId: '2',
      title: '书1',
      price: 111,
      description: '描述2',
      cover: '封面2',
      detail: '详情2',
      quantity: 2,
    },
    {
      productId: '3',
      title: '书2',
      price: 111,
      description: '描述3',
      cover: '封面3',
      detail: '详情3',
      quantity: 1,
    },
  ],
  total: 0,
  totalAmount: 0,
});

// 计算购物车内商品总价
const sumPrice = computed(() => {
  const total = cartData.value.items.reduce((accumulator, currentItem) => {
    return accumulator + currentItem.price * currentItem.quantity;
  }, 0);

  // 更新购物车的 total 和 totalAmount
  cartData.value.total = total;
  cartData.value.totalAmount = cartData.value.items.length;

  return total;
});

// 搜索模块
const searchInput = ref('');
function clickSearch(){
  ElMessage.success('搜索按钮已点击！');
  try {
    getSpecificProduct(searchInput.value).then((res) => {
      searchInput.value = '';
      // 此处应该修改，先确定有没有该书籍
      router.push('/book-detail/' + searchInput.value);
    })
  }catch (error) {
    ElMessage.error('该书籍不存在！');
  }
}

// 添加商品模块
function addBook() {
  ElMessage.success('添加商品按钮已点击！');
  // 处理specifications部分
  addSpecification('作者', author.value);
  addSpecification('副标题', subtitle.value);
  addSpecification('ISBN', ISBN.value);
  addSpecification('装帧', bindingAndLayout.value);
  addSpecification('页数', pages.value);
  addSpecification('出版社', publishingHouse.value);
  addSpecification('出版日期', publicationDate.value);
  try {
    addProduct(bookInfo).then((res) => {
      if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
        ElMessage({
          message: "创建成功！",
          type: 'success',
          center: true,
        })
      } else if (res.data.code === '400') {
        ElMessage({
          message: res.data.msg,
          type: 'error',
          center: true,
        })
      }
      centerDialogVisible2.value = false;
    })
  } catch (error) {
    ElMessage.error('添加商品失败！');
  }
  centerDialogVisible2.value = false;
}

function clickAvatar(){
  ElMessage.success('头像已点击');
}

function clickMessage(){
  ElMessage.success('消息按钮已点击！');
}
// 点击商品下加号触发加入购物车
function clickBuy(bookInformation: ProductVO){
  const newCartItem : CartItems = {
    productId: bookInformation.id,
    title: bookInformation.title,
    price: bookInformation.price,
    description: bookInformation.description,
    cover: bookInformation.cover,
    detail: bookInformation.detail,
    quantity: 1,
  }
  onAddItem(newCartItem);
  ElMessage.success('已加入购物车！');
}

function submit(){
  ElMessage.success('结账按钮已点击！');
}

const goToBookDetail = (bookId : string) => {
  router.push('/book-detail/' + bookId);
}

// 购物车删除行
const deleteRow = (index: number) => {
  if (index >= 0 && index < cartData.value.items.length) {
    cartData.value.items.splice(index, 1);
  }
};

// 购物车添加行
const onAddItem = (book: CartItems) => {
  // 检查是否已有相同商品
  const existingItemIndex = cartData.value.items.findIndex(
      (item) => item.productId === book.productId
  );

  if (existingItemIndex !== -1) {
    // 如果商品已存在，增加数量
    cartData.value.items[existingItemIndex].quantity += book.quantity;
  } else {
    // 如果商品不存在，添加新商品
    cartData.value.items.push(book);
  }
};

// 处理商品图片上传
const beforeImageUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('头像必须是jpg/jpeg格式!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片不应超过2MB!');
    return false;
  }
  customUpload(rawFile);
  return false;
};
function customUpload(file: File){
  // 需要从后端传入工具类
  // uploadFile(file).then((res) => {
  //   handleUploadSuccess(res);
  // }).catch((err) => {
  //   handleUploadError(err);
  // })
}
</script>

<template>
  <el-container>
    <el-aside width="300px" style="background-color: #565656">
      <el-card style="width: 100%; height: 80px; background-color: #2e2d2e; border: 0px solid black;">
        <div class="card-header">
          <span class="title" style="color: white; font-weight: bold;font-size: x-large;margin-left: 10px;margin-top: 1px">番茄书城</span>
        </div>
      </el-card>
      <div style="margin-left: 10px;margin-right: 10px;margin-top: 20px">
        <div style="display:flex">
          <!--购物车模块-->
          <el-icon style="vertical-align: middle; height:25px; color: white">
            <shopping-cart/>
          </el-icon>
          <div style="color: white; font-weight: bold; margin-left: 10px">购物车</div>
        </div>
        <el-table :data="cartData.items" style="width: 100%; margin-top: 20px;" max-height="500" class="transparent-table">
          <el-table-column prop="title" label="名称" width="90" />
          <el-table-column prop="price" label="价格" width="70" />
          <el-table-column prop="quantity" label="数量" width="60" />
          <el-table-column prop="remove" label="操作" min-width="60">
            <template #default="scope">
              <el-button
                  link
                  type="primary"
                  size="small"
                  @click.prevent="deleteRow(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px; margin-left: 10px; color: white">合计: {{sumPrice}}元</div>
        <el-button @click="centerDialogVisible1 = true;" style="color: white; background-color: #2e2d2e; border-color: #2e2d2e; margin-top: 50px; margin-left: 75px; width: 100px">
          结账
        </el-button>
      </div>
    </el-aside>
    <el-container style="display: flex; flex-direction: column">
      <div class="page">
        <el-card>
          <!--卡片首部模块-->
          <template #header>
            <div class="card-header">
              <span class="title" style="font-weight: bold;font-size: large;margin-left: 10px;margin-top: 6px">主页</span>
              <el-input
                  v-model="searchInput"
                  style="max-width: 600px; margin-top: 2px; margin-left: 30%;"
                  placeholder="请输入书籍ID"
              >
                <template #append>
                  <el-button type="primary" class="search" @click="clickSearch">
                    <el-icon style="vertical-align: middle">
                      <Search/>
                    </el-icon>
                  </el-button>
                </template>
              </el-input>
              <el-avatar :size="40" :src="avatarUrl" style="margin-left: 50px" @click="clickAvatar"/>
              <el-button class="search" @click="clickMessage" style="margin-left: 20px; height: 40px; width: 40px; border-radius: 40%">
                <el-icon style="vertical-align: middle">
                  <BellFilled/>
                </el-icon>
              </el-button>
            </div>
          </template>
          <!--卡片推荐模块-->
          <div style="font-weight: bold; font-size: xx-large">今日推荐</div>
          <div style="margin-top: 10px">
            <el-carousel :interval="4000" type="card" height="300px">
              <el-carousel-item v-for="item in advertisementList" :key="item.productId">
                <div style="height: 100%; width: 100%; display: flex">
                  <div style="width: 50%">
                    <div class="advertisement-book-name" style="height: 35%; width: 90%; margin-top: 20px;margin-left: 20px">{{item.title}}</div>
                    <el-text class="advertisement-book-content" style="margin-left: 20px; width: 80%">{{item.content}}</el-text>
                    <el-button type="warning" disabled style="margin-top: 10px">BUY NOW!</el-button>
                  </div>
                  <div style="width: 50%">
                    <el-image
                        :src="item.imageUrl"
                        :alt="item.title"
                        fit="cover"
                        style="height: 80%; width: 70%; margin-left: 25px; margin-top: 25px"
                    >
                      <template #error>
                        <div class="image-error">图片加载失败</div>
                      </template>
                    </el-image>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>
          <div style="margin-top: 70px; height: 60px; display: flex">
            <div style="font-weight: bold; font-size: xx-large">商品列表</div>
            <el-button @click="centerDialogVisible2 = true;" type="info" style="margin-left: 900px; margin-top: 15px">添加商品</el-button>
          </div>
        </el-card>
      </div>
      <!--      商品列表       -->
      <div style="width: 90%; height: 100%; margin-left: 60px">
        <el-row :gutter="30">
          <el-col
              v-for="book in bookList"
              :key="book.id"
              :xs="24"
              :sm="12"
              :md="6"
              :lg="6"
              :xl="6"
          >
            <el-card @click="goToBookDetail(book.id)" class="book-card" shadow="hover" style="height: 360px; margin-top: 20px">
              <template #header>
                <div class="book-header">
                  <el-image
                      class="book-image"
                      :src="book.cover"
                      :alt="book.title"
                      fit="cover"
                  >
                    <template #error>
                      <div class="image-error">图片加载失败</div>
                    </template>
                  </el-image>
                </div>
              </template>
              <div class="book-name">{{ book.title }}</div>
              <el-rate
                  v-model="book.rate"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value} points"
              />
              <div style="display: flex">
                <div class="book-price">￥{{book.price}}</div>
                <el-button class="buy-button" @click.stop="clickBuy(book)" style="margin-left: 20px; height: 40px; width: 40px; border-radius: 50%">
                  <el-icon style="vertical-align: middle">
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-container>
  </el-container>

  <!--购物车提交弹出窗口-->
  <el-dialog v-model="centerDialogVisible1" title="提示" width="500" center>
    <span style="display: flex">
      <el-icon size="large" style="margin-left: 20px;margin-top: 2px"><ShoppingCartFull /></el-icon>
      <div style="margin-left: 15px">你确定要提交账单吗？</div>
    </span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="centerDialogVisible1 = false">取消</el-button>
        <el-button type="primary" @click="submit">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!--创建商品弹出窗口-->
  <el-dialog v-model="centerDialogVisible2" title="创建商品" width="1000" center>
    <span style="display: flex">
      <div>
        <el-main>
        <el-form :model="bookInfo" label-width="auto" style="margin-top: 10px">
          <div style="display: flex;">
            <el-form-item label="商品名称" style="width: 300px; display: flex">
              <el-input v-model="bookInfo.title" placeholder="请输入商品名称"/>
            </el-form-item>
            <el-form-item label="副标题" style="width: 300px; display: flex">
              <el-input v-model="subtitle" placeholder="请输入副标题"/>
            </el-form-item>
            <el-form-item label="作者" style="width: 300px; display: flex">
              <el-input v-model="author" placeholder="请输入作者名字"/>
            </el-form-item>
          </div>
          <div style="display: flex; margin-top: 25px">
            <el-form-item label="商品价格" style="width: 300px;">
              <el-input v-model="bookInfo.price" placeholder="请输入商品价格（单位：元）"/>
            </el-form-item>
            <el-form-item label="商品评分" style="width: 300px">
              <el-rate v-model="bookInfo.rate" allow-half />
            </el-form-item>
            <el-form-item label="ISBN" style="width: 300px">
              <el-input v-model="ISBN" placeholder="请输入书籍ISBN"/>
            </el-form-item>
          </div>
          <el-form-item label="商品描述" style="width: 900px; margin-top: 25px">
            <el-input v-model="bookInfo.description" type="textarea" placeholder="请输入商品描述" :rows="2" />
          </el-form-item>
          <div style="display: flex; margin-top: 25px">
            <el-form-item label="商品图片">
              <el-upload
                  class="avatar-uploader"
                  :show-file-list="false"
                  :before-upload="beforeImageUpload"
                  :limit="1"
              >
                <img v-if="bookInfo.cover" :src="bookInfo.cover" class="avatar"/>
                <el-icon v-else class="avatar-uploader-icon"><Plus/></el-icon>
              </el-upload>
            </el-form-item>
            <div>
              <div style="display: flex; margin-top: 15px">
                <el-form-item label="装帧" style="width: 250px;">
                  <el-input v-model="bindingAndLayout" placeholder="装帧"/>
                </el-form-item>
                <el-form-item label="出版社" style="width: 400px;">
                  <el-input v-model="publishingHouse" placeholder="出版社"/>
                </el-form-item>
              </div>
              <div style="display: flex; margin-top: 15px">
                <el-form-item label="页数" style="width: 250px;">
                  <el-input v-model="pages" placeholder="页数"/>
                </el-form-item>
                <el-form-item label="出版日期">
                  <el-date-picker
                      v-model="publicationDate"
                      type="date"
                      placeholder="选择日期"
                      :size="'default'"
                  />
                </el-form-item>
              </div>
            </div>
          </div>
          <el-form-item label="商品详细说明" style="width: 900px; margin-top: 25px">
            <el-input v-model="bookInfo.detail" type="textarea" placeholder="请输入商品详细说明" :rows="5" />
          </el-form-item>
        </el-form>
      </el-main>
      </div>
    </span>
    <template #footer>
      <div class="dialog-footer" style="width: 1000px;">
        <el-button @click="addBook" type="primary" >
          确定
        </el-button>
        <el-button @click="centerDialogVisible2 = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

.page {
  height: 1000px;
  width: 100%;
}

.card-header{
  height: 40px;
  display: flex;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 250px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #565656;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #dddddd;
}

.book-card {
  transition: transform 0.3s ease;
}

.book-card:hover {
  transform: translateY(-5px);
}

.book-header {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 190px;
}

.book-image{
  width: 180px;
  height: 180px;
  object-fit: cover;
}

.book-name {
  font-weight: bold;
  font-size: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.advertisement-book-name {
  font-weight: bold;
  font-size: xx-large;
  word-break: break-word; /* 允许长单词换行 */
  overflow: hidden; /* 溢出部分隐藏 */
  text-overflow: ellipsis; /* 溢出部分显示省略号 */
  display: -webkit-box; /* 使用 Webkit 内核的 box 布局 */
  -webkit-box-orient: vertical; /* 垂直排列 */
  -webkit-line-clamp: 2; /* 限制显示 2 行 */
  line-height: 1.5; /* 设置行高，确保文字不重叠 */
}

.advertisement-book-content{
  word-break: break-word; /* 允许长单词换行 */
  overflow: hidden; /* 溢出部分隐藏 */
  text-overflow: ellipsis; /* 溢出部分显示省略号 */
  display: -webkit-box; /* 使用 Webkit 内核的 box 布局 */
  -webkit-box-orient: vertical; /* 垂直排列 */
  -webkit-line-clamp: 5; /* 限制显示 5 行 */
  line-height: 1.5; /* 设置行高，确保文字不重叠 */
}

.book-price {
  width: 150px;
  margin-top: 5px;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.transparent-table {
  --el-table-bg-color: rgba(0, 0, 0, 0); /* 表格背景色透明 */
  --el-table-border-color: rgba(0, 0, 0, 0.2); /* 表格边框颜色半透明 */
  --el-table-header-bg-color: rgba(0, 0, 0, 0); /* 表头背景色透明 */
  --el-table-header-font-color: #ffffff; /* 表头文字颜色 */
  --el-table-text-color: #ffffff; /* 表格文字颜色 */
  --el-table-tr-bg-color: rgba(0, 0, 0, 0); /* 表格行背景色透明 */
  --el-table-current-row-bg-color: rgba(0, 0, 0, 0.2); /* 当前行背景色半透明 */
  --el-table-hover-row-bg-color: rgba(0, 0, 0, 0.1); /* 悬停行背景色半透明 */
}

.avatar-uploader{
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  height: 120px;
  width: 120px;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>