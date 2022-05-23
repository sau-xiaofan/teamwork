<template>
  <div>

    <!-- #707070 -->
    <vue-particles
      class="login-background"
      color="#97D0F2"
      :particleOpacity="0.7"
      :particlesNumber="50"
      shapeType="circle"
      :particleSize="4"
      linesColor="#97D0F2"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push">
    </vue-particles>
    <div class="login-other">
      <el-form :rules="rules" ref="registerorm" :model="registerorm" class="loginContainer">
        <el-form-item style="width:100%">
        <i class="el-icon-back"  style=" font-size: 20px;" @click="routeto"></i>
          <a class="loginTitle">手机号注册</a>
        </el-form-item>
        <el-form-item prop="phone">
          <span style="size: A5">手机号</span>
          <el-select size="normal" style="width: 80px" v-model="phoneCity">
            <el-option v-for="phoneCity in phoneCitys" :label="phoneCity.name" :value="phoneCity.name" :key="phoneCity.id"></el-option>
          </el-select>
          <el-input  ref="phone" size="normal" type="text" v-model="registerorm.phone" style="width: 210px"
                    auto-complete="on"
                    placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <span style="size: A5">验证码</span>
          <el-input  ref="code" size="normal" type="text" v-model="registerorm.code" style="width: 150px"
                     auto-complete="on"
                     placeholder="请输入手机验证码"></el-input>
          <el-button type="primary" @click="sendMessage('registerorm')" :disabled="sendModel.countFlag">{{sendModel.btnMsg == null ? sendModel.countNum+'s后重新发送' : sendModel.btnMsg}}</el-button>
        </el-form-item>
        <el-form-item style="height: 30px" prop="checked">
        <el-checkbox size="normal" class="loginRemember" v-model="checked">注册即表示您已阅读并同意
        </el-checkbox>
        <el-link :underline="false" size="normal" type="primary" @click="checkLink">
          《用户使用协议》
        </el-link>
        </el-form-item>
        <el-button size="normal"  type="primary" style="width: 100%;" @click="submitRegister('registerorm')">注册</el-button>
      </el-form>
    </div>
  </div>

</template>

<script>

export default {
  name: "register",
  data() {
    return {
      sendModel: {
        // 倒计时周期
        countNum: 60,
        // 用于倒计时标记，true-正在倒计时
        countFlag: false,
        // 定时器
        intervalBtn: { },
        // 默认按钮的值
        btnMsg: '发送短信'
      },
      phoneCity: '',
      phoneCitys: [
        {
          value: '1',
          id: '+86',
          name: '中国'
        },
        {
          value: '2',
          id: '+886',
          name: '台湾'
        },
        {
          value: '3',
          id: '+852',
          name: '香港'
        },
        {
          value: '4',
          id: '+853',
          name: '澳门'
        },
      ],
      registerorm: {
        phone: '',
        code: '',
      },
      checked: false,
      rules: {
        phone: [{ required: true, message: '手机号不能为空' },
          { type: 'number',
            message: '手机号格式不正确',
            trigger: 'blur',
            transform(value) {
              var phonereg = 11 && /^((13|14|15|16|17|18|19)[0-9]{1}\d{8})$/
              if (!phonereg.test(value)) {
                return false
              } else {
                return Number(value)
              }
            }
          }],
        code: [{required: true, message: '请输入验证码', trigger: 'blur'}],

      }
    }
  },
  methods: {
    checkLink() {
      let routeData = this.$router.resolve({ path: '/xieyi'});
      window.open(routeData.href, '_blank');
    },
    routeto() {
      this.$router.push('/')
    },
    sendMessage(formName) {
      var door = false
      this.$refs.registerorm.validateField("phone", errMsg => {
        if (errMsg) {
          door = true;
          return;
        }
      });
      if(door === true) return;
      const param = {
        phone: this.registerorm.phone,
        phoneCity: this.phoneCity
      };
      this.$http.post
         ('/user/sendMassage', param)
        .then((res) => {
            if(res.data.result === "success") {
              this.$message({
                message: '消息发送成功',
                type: 'success'
              });
              this.sendModel.btnMsg = null
              // 更改btn状态
              this.sendModel.countFlag = !this.sendModel.countFlag
              // 设置倒计时
              this.sendModel.intervalBtn = setInterval(() => {
                if (this.sendModel.countNum <= 0) {
                  // 重置btn提示信息
                  this.sendModel.btnMsg = '获取验证码'
                  // 清除定时器
                  clearInterval(this.sendModel.intervalBtn)
                  // 更改btn状态
                  this.sendModel.countFlag = !this.sendModel.countFlag
                  // 重置倒计时状态
                  this.sendModel.countNum = 60
                };
                // 倒计时
                this.sendModel.countNum--
              }, 1000)
            } else {
              this.$message({
                message: res.data.message,
                type: 'error'
              });
            }
        })
    },
    submitRegister(formName) {
      var door = false
      this.$refs[formName].validate((valid) => {
        if (valid) {
        } else {
          door = true
          return false;
        }
      });
      if(door === true) return;
      if(this.checked === false) {
        this.$message({
          message: "请点击同意用户协议",
          type: 'warning'
        });
        return;
      }
      const param = {
        phone: this.registerorm.phone,
        password: this.$md5(this.registerorm.phone),
        code: this.registerorm.code
      };
      this.$http.post
      ('/user/register', param)
        .then((res) => {
          if(res.data.result === "success") {
            this.$message({
              message: '注册成功',
              type: 'success'
            });
            this.$router.push('/')
          } else {
            this.$message({
              message: res.data.message,
              type: 'error'
            });
          }
        })
    },
  },
  created() {
    this.phoneCity = this.phoneCitys[0].name

  }
}
</script>

<style>
.login-background {
  background: linear-gradient(-180deg, #dcf2e6 0%, #ffffff 100%);
  width: 100%;
  height: 100%; /**宽高100%是为了图片铺满屏幕 */
  z-index: -1;
  position: absolute;
}

.login-other {
  z-index: 1;
  margin: 180px 0 0 calc(calc(100vw - 410px) / 2);
  position: absolute;
}

.loginContainer {
  border-radius: 14px;
  background-clip: padding-box;
  width: 350px;
  padding: 15px 35px 30px 35px;
  background: #fefefe;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.loginTitle {
  right: 100px;
  margin: 15px auto 20px auto;
  text-align: center;
  color: #707070;
  font-size: medium;
  margin-left: 110px;
}

.loginRemember {
  text-align: left;
  margin: 0 0 15px 0;
}

</style>
