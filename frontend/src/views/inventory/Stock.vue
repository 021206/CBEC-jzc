<template>
  <div>
    <h2 style="margin-bottom: 20px;">实时库存</h2>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="商品">
        <el-select v-model="searchForm.productId" placeholder="请选择商品" clearable>
          <el-option
              v-for="item in productList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库">
        <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable>
          <el-option
              v-for="item in warehouseList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 库存列表 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="productId" label="商品ID" width="100" />
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column prop="warehouseId" label="仓库ID" width="100" />
      <el-table-column prop="warehouseName" label="仓库名称" />
      <el-table-column prop="quantity" label="当前库存" width="120">
        <template #default="{ row }">
          <el-tag :type="row.quantity < 10 ? 'danger' : 'success'">
            {{ row.quantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="version" label="版本号" width="100" />
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end;"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getAllProducts } from '@/api/product.js';
import { getAllWarehouses } from '@/api/warehouse.js';

// 商品和仓库下拉选项
const productList = ref([]);
const warehouseList = ref([]);

const searchForm = reactive({
  productId: null,
  warehouseId: null
});

const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

// 加载下拉数据
const loadOptions = async () => {
  const [products, warehouses] = await Promise.all([
    getAllProducts(),
    getAllWarehouses()
  ]);
  if (products.code === 200) productList.value = products.data || [];
  if (warehouses.code === 200) warehouseList.value = warehouses.data || [];
};

// 加载库存数据
const loadData = async () => {
  // 这里调用后端库存列表接口（目前后端还没有实现 /inventory/list，先模拟）
  // 实际开发时需要后端提供分页查询接口
  // 目前先用占位数据，后续补充
  console.log('查询条件:', searchForm);
  // TODO: 等后端接口就绪后替换为真实调用
  // const res = await getInventoryList({ ...searchForm, pageNum: pageNum.value, pageSize: pageSize.value });
  // if (res.code === 200) { tableData.value = res.data.list; total.value = res.data.total; }
};

const resetSearch = () => {
  searchForm.productId = null;
  searchForm.warehouseId = null;
  pageNum.value = 1;
  loadData();
};

onMounted(() => {
  loadOptions();
  loadData();
});
</script>