<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>用户注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="100px">
        <!-- 🌟 新增用户身份选择 -->
        <el-form-item label="用户身份" prop="role" class="role-selector">
          <el-radio-group v-model="registerForm.role">
            <el-radio-button :value="'admin'">管理员</el-radio-button>
            <el-radio-button :value="'user'">普通用户</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <!-- 🌟 调整字段顺序符合常见注册流程 -->
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="8-16位字母/数字/下划线"
              clearable
          />
        </el-form-item>

        <el-form-item label="设置密码" prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              show-password
              placeholder="至少8位，包含大小写+数字+特殊字符"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              show-password
              placeholder="请再次输入密码"
          />
        </el-form-item>

        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入身份证姓名" />
        </el-form-item>

        <el-form-item label="头像上传" prop="avatar">
          <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <template v-if="!registerForm.avatar">
              <el-button type="primary" icon="el-icon-upload">
                点击上传头像
              </el-button>
              <div class="upload-tip">支持JPG/PNG格式，小于2MB</div>
            </template>
            <el-image
                v-else
                :src="registerForm.avatar"
                class="avatar-preview"
                fit="cover"
            />
          </el-upload>
        </el-form-item>

        <!-- 可选信息分组 -->
        <el-collapse>
          <el-collapse-item title="可选信息">
            <el-form-item label="电子邮箱" prop="email">
              <el-input v-model="registerForm.email" placeholder="name@example.com" />
            </el-form-item>

            <el-form-item label="手机号码" prop="telephone">
              <el-input v-model="registerForm.telephone" placeholder="11位手机号码" />
            </el-form-item>

            <el-form-item label="所在地区" prop="location">
              <el-input v-model="registerForm.location" placeholder="省/市/区" />
            </el-form-item>
          </el-collapse-item>
        </el-collapse>

        <el-form-item class="action-buttons">
          <el-button
              type="primary"
              size="large"
              @click="handleRegister"
          >
            立即注册
          </el-button>
          <div class="login-link">
            已有账号？<el-link type="primary" @click="goToLogin">立即登录</el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 🌟 头像预览弹窗 -->
    <el-dialog v-model="avatarPreviewVisible" title="头像预览" width="30%">
      <img :src="registerForm.avatar" class="avatar-preview" v-if="registerForm.avatar">
      <span v-else>请输入有效的图片URL</span>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {register} from "@/api/accounts";
import { axios } from '../../utils/request'; // 新增引入
import { debounce } from 'lodash'
import { ElMessage } from 'element-plus';


const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  role: '',
  telephone: '',
  email: '',
  location: '',
  avatar: ''
})
const router = useRouter();
const avatarPreviewVisible = ref(false);

// 🌟 强化用户名格式验证
const usernameRegex = /^[a-zA-Z0-9_-]{8,16}$/;
// 严格匹配示例中的邮箱格式：GBTxIYBbGsaX@ItfYfHKkKBrH.com
const emailRegex = /^[A-Za-z0-9]{1,20}@[A-Za-z0-9]{1,10}(\.[A-Za-z]{2,6})+$/;
// 精确匹配示例中的手机号：15943688232
const phoneRegex = /^1[3-9]\d{9}$/;

// 🌟 优化密码复杂度提示
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

const goToLogin = () => {
  router.push({
    path: '/login',
    query: { from: 'register' } // 可选：携带来源参数
  })
}



const rules = {
  role: [
    {
      required: true,
      message: '必须选择用户身份',
      trigger: ['change', 'blur']
    }
  ],
  username: [
    {
      required: true,
      message: '用户名不能为空',
      trigger: 'blur'
    },
    {
      pattern: /^[a-zA-Z0-9_-]{8,16}$/,
      message: '8-16位（字母/数字/_-）',
      trigger: 'blur'
    },
    /*{
      validator: (rule, value, callback) => {
        if (!value || value.length < 8) return callback() // 跳过空值或长度不足的校验
        debouncedCheck(value, callback)
      },
      trigger: 'blur'
    }*/
  ],
  password: [
    {
      validator: (_, value, callback) => {
        if (!value) {
          callback(new Error('密码不能为空'));
        } else if (!passwordRegex.test(value)) {
          callback(new Error('需包含大小写字母、数字和特殊符号'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true,
      message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  name: [{ required: true,
    message: '请输入姓名', trigger: 'blur' }],
  email: [
    {
      validator: (rule, value, callback) => {
        if (value && !emailRegex.test(value)) {
          callback(new Error('邮箱格式不正确'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  telephone: [
    {
      validator: (rule, value, callback) => {
        if (value && !phoneRegex.test(value)) {
          callback(new Error('手机号格式不正确'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

// 修改后的用户名校验函数
const checkUsername = async (username) => {
  try {
    const response = await axios.get(`/api/accounts/${username}`, {
      timeout: 5000
    });
    // 假设后端对不存在的用户返回data: null
    return response.data.data === null;
  } catch (error) {
    if (error.response?.status === 404) {
      return true; // 用户不存在
    }
    throw new Error('校验失败');
  }
};

const debouncedCheck = debounce(async (username, callback) => {
  try {
    const available = await checkUsername(username)
    available ? callback() : callback(new Error('用户名已存在'))
  } catch (err) {
    callback(new Error(err.message))
  }
}, 800)


// 上传前校验
const beforeAvatarUpload = (file) => {
  const isImage = ['image/jpeg', 'image/png'].includes(file.type);
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('仅支持 JPG/PNG 格式!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
}

// 上传成功处理
const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    registerForm.value.avatar = res.data.url
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}
const handlePreviewAvatar = () => {
  if(registerForm.value.avatar){
    avatarPreviewVisible.value = true;
  } else {
    ElMessage.warning('请先输入头像地址');
  }
};
// 修改handleRegister方法
const handleRegister = async () => {
  try {
    // 构造符合后端要求的payload（注意移除前端特有的confirmPassword）
    const { confirmPassword, ...submitData } = registerForm.value

    const res = await register(submitData)
    console.log(res)
    if (res.data && res.data.userId) {
      sessionStorage.setItem('userId', res.data.userId);
    }
    if (res.code === '000' || res.code === '200') {
      ElMessage.success('注册成功')
      router.push('/login')
    } else {
      ElMessage.warning(res.msg || '注册失败')
    }
  } catch (error) {
    // 统一错误处理
    const errorMsg = error.response?.data?.msg ||
        error.message ||
        '注册服务不可用'
    ElMessage.error(errorMsg)

    // 调试用（上线前移除）
    console.error('完整错误:', error)
  }
}
</script>


<style scoped>
.role-selector {
  margin-bottom: 24px;
  ::v-deep .el-form-item__content {
    justify-content: center;
  }
}

.avatar-preview {
  width: 100%;
  border-radius: 4px;
}

.action-buttons {
  margin-top: 32px;
  ::v-deep .el-form-item__content {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }
}
/* 上传按钮样式 */
.avatar-uploader {
  .el-upload {
    width: 100%;
    .el-button {
      width: 200px;
      height: 40px;
    }
  }

  .upload-tip {
    color: #909399;
    font-size: 12px;
    margin-top: 8px;
  }

  .avatar-preview {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  }
}

.login-link {
  font-size: 14px;
}
</style>