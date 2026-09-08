<template>
  <div>
    <h2 style="margin-bottom: 20px;">库存流水</h2>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" style="margin-bottom: 20px;">
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
      <el-form-item label="变动类型">
        <el-select v-model="searchForm.changeType" placeholder="全部" clearable>
          <el-option label="入库" :value="1" />
          <el-option label="出库" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px;"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="单据号" width="180" />
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column prop="warehouseName" label="仓库名称" />
      <el-table-column prop="changeType" label="变动类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.changeType === 1 ? 'success' : 'danger'">
            {{ row.changeType === 1 ? '入库' : '出库' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="changeQty" label="变动数量" width="120" />
      <el-table-column prop="beforeQty" label="变动前" width="120" />
      <el-table-column prop="afterQty" label="变动后" width="120" />
      <el-table-column prop="operator" label="操作人" width="120" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="createTime" label="操作时间" width="180" />
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
import { ElMessage } from 'element-plus';
import { getAllProducts } from '@/api/product.js';
import { getAllWarehouses } from '@/api/warehouse.js';
import { getInventoryLogPage } from '@/api/inventory.js';

const productList = ref([]);
const warehouseList = ref([]);

const searchForm = reactive({
  productId: null,
  warehouseId: null,
  changeType: null
});

const dateRange = ref([]);

const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

// 加载下拉数据
const loadOptions = async () => {
  try {
    const [products, warehouses] = await Promise.all([
      getAllProducts(),
      getAllWarehouses()
    ]);
    if (products.code === 200) productList.value = products.data || [];
    if (warehouses.code === 200) warehouseList.value = warehouses.data || [];
  } catch (error) {
    console.error('加载下拉数据失败', error);
  }
};

// 加载流水数据
const loadData = async () => {
  try {
    const params = {
      productId: searchForm.productId,
      warehouseId: searchForm.warehouseId,
      changeType: searchForm.changeType,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    };
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0];
      params.endTime = dateRange.value[1];
    }
    const res = await getInventoryLogPage(params);
    if (res.code === 200) {
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    } else {
      ElMessage.error(res.msg || '加载流水失败');
    }
  } catch (error) {
    console.error('加载流水失败', error);
    ElMessage.error('网络异常，请稍后重试');
  }
};

const resetSearch = () => {
  searchForm.productId = null;
  searchForm.warehouseId = null;
  searchForm.changeType = null;
  dateRange.value = [];
  pageNum.value = 1;
  loadData();
};

onMounted(() => {
  loadOptions();
  loadData();
});
</script>