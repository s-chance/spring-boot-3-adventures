## 大事记系统前端

### 前置知识

#### JS 的导入导出

- JS 提供的导入导出机制，可以实现按需导入。

  先看原始的导入方式

  `showMessage.js`

  ```js
  // 简单的展示信息
  function simpleMessage(msg) {
      console.log(msg)
  }
  
  // 复杂的展示信息
  function complexMessage(msg) {
      console.log(new Date() + ": " + msg)
  }
  ```

  `message.html`

  ```html
  <body>
      <div>
          <button id="btn">展示信息</button>
      </div>
      <!-- 导入 showMessage.js 文件的全部内容 -->
      <script src="showMessage.js"></script>
      
      <script>
          document.getElementById("btn").onclick = function() {
              complexMessage('click me');
          }
      </script>
  </body>
  ```

  这个时候 html 只使用到了`complexMessage`的函数，但是导入的是整个 js 文件，会有性能损失。

  使用`export`就可以按需导出 js 中的函数

  ```js
  // 简单的展示信息
  export function simpleMessage(msg) {
      console.log(msg)
  }
  
  // 复杂的展示信息
  export function complexMessage(msg) {
      console.log(new Date() + ": " + msg)
  }
  ```

  再在 html 中使用`import`按需导入 js 中的函数

  ```html
  <body>
      <div>
          <button id="btn">展示信息</button>
      </div>
      
      <script type="module">
          import { complexMessage } from './showMessage.js';
          document.getElementById("btn").onclick = function() {
              complexMessage('click me');
          }
      </script>
  </body>
  ```

  但是当 js 中的函数过多时，对每个函数使用`export`的方法就不太合适，这就可以使用批量导出的方式

  ```js
  // 简单的展示信息
  function simpleMessage(msg) {
      console.log(msg)
  }
  
  // 复杂的展示信息
  function complexMessage(msg) {
      console.log(new Date() + ": " + msg)
  }
  // 批量导出
  export { simpleMessage,complexMessage }
  ```

  如果觉得函数名字过长还可以在导入的时候使用`as`取别名

  ```html
  <body>
      <div>
          <button id="btn">展示信息</button>
      </div>
      
      <script type="module">
          import { complexMessage as cm } from './showMessage.js';
          document.getElementById("btn").onclick = function() {
              cm('click me');
          }
      </script>
  </body>
  ```

  也可以在导出的时候取别名，在导入的时候直接用别名导入

  ```js
  export { simpleMessage as sm,complexMessage as cm }
  ```

  ```html
  <script type="module">
      import { cm } from './showMessage.js';
      document.getElementById("btn").onclick = function() {
          cm('click me');
      }
  </script>
  ```

  但是在导出函数比较多的情况下，也不方便记忆所有的函数名或别名，这就可以使用 js 提供的默认导出的方式。

  ```js
  export default { simpleMessage,complexMessage }
  ```

  默认导出的内容在 html 中不需要再使用`{}`花括号包围，可以取一个新的名字，代表了 js 中默认导出的所有内容，使用的时候调用其中的指定函数即可。

  ```html
  <script type="module">
      import messageMethods from './showMessage.js';
      document.getElementById("btn").onclick = function() {
          messageMethods.complexMessage('click me');
      }
  </script>
  ```

  #### 小结

  导出

  - export function....
  - export { func1,fun2,.... }
  - export default { .... }

  导入

  ```html
  <script type="module">
      import { .... } from '....'
  </script>
  ```

  - 默认导出的内容导入时不需要加`{}`
  - 非默认导出的内容导入时需要加`{}`

  导入导出可以使用`as`关键词取别名



### Vue

Vue 是一款用于构建用户界面的渐进式的 JavaScript 框架。

Vue 的核心包有声明式渲染、组件系统，往外拓展还有客户端路由、状态管理，构建工具。

Vue 核心包可以引入现有的项目中进行局部模块改造，也可以结合 Vue 插件进行工程化开发，实现整站开发。

由于 Vue 即可用于局部开发，也可以用于整站开发，所以是渐进式的框架。



### 局部使用 Vue

#### 快速入门

在页面上输出`hello vue3`

原生 js

```js
let msg="hello vue3";
documnet.getElementById("元素的 id 属性值").innerHTML=msg;
```

使用 Vue

准备

- html 引入 Vue 模块

  ```html
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
  </script>
  ```

- 创建 Vue 程序的应用实例

  ```html
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      
      createApp({
          
      })
  </script>
  ```

- 准备元素 div，使用 Vue 控制

  ```html
  <div id="app">
  
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
  
      createApp({
  
      }).mount("#app")
  </script>
  ```



构建用户界面

- 准备数据

  ```html
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
  
      createApp({
          data() {
              return {
                  msg: "hello vue3"
              }
          }
      }).mount("#app")
  </script>
  ```

- 通过插值表达式渲染页面

  ```html
  <div id="app">
      <h1>{{ msg }}</h1>
  </div>
  ```

  

#### 常用指令

指令：html 标签上带有 v- 前缀的特殊属性，不同的指令具有不同的含义，可以实现不同的功能。

常用指令：

|         指令          |                         作用                          |
| :-------------------: | :---------------------------------------------------: |
|         v-for         |        列表渲染，遍历容器的元素或者对象的属性         |
|        v-bind         |    为 html 标签绑定属性值，如设置 href，css 样式等    |
| v-if/v-else-if/v-else |  条件性地渲染某元素，判定为 true 时渲染，否则不渲染   |
|        v-show         | 根据条件展示某元素，区别在于切换的是 display 属性的值 |
|        v-model        |             在表单元素上创建双向数据绑定              |
|         v-on          |                 为 html 标签绑定事件                  |



##### v-for

- 作用：列表渲染，遍历容器的元素或者对象的属性

- 语法：`v-for = "(item,index) in items"`

  - 参数说明：
    - `items`为遍历的数组
    - `item`为遍历的元素
    - `index`为索引/下标，从 0 开始。可以省略 index，语法：`v-for = "item in items"`

- 原始 html

  ```html
  <div id="app">
      <table border="1 solid" colspa="0", cellspacing="0">
          <tr>
              <th>title</th>
              <th>category</th>
              <th>time</th>
              <th>state</th>
              <th>operate</th>
          </tr>
          <tr>
              <td>title1</td>
              <td>category1</td>
              <td>2000-01-01</td>
              <td>published</td>
              <td>
                  <button>edit</button>
                  <button>delete</button>
              </td>
          </tr>
          <tr>
              <td>title2</td>
              <td>category2</td>
              <td>2012-03-11</td>
              <td>published</td>
              <td>
                  <button>edit</button>
                  <button>delete</button>
              </td>
          </tr>
          <tr>
              <td>title3</td>
              <td>category3</td>
              <td>2022-12-01</td>
              <td>published</td>
              <td>
                  <button>edit</button>
                  <button>delete</button>
              </td>
          </tr>
      </table>
  </div>
  ```

- 使用`v-for`优化代码

  ```html
  <div id="app">
      <table border="1 solid" colspa="0" cellspacing="0">
          <tr>
              <th>title</th>
              <th>category</th>
              <th>time</th>
              <th>state</th>
              <th>operate</th>
          </tr>
          <!-- v-for 添加到需要重复出现的元素上 -->
          <tr v-for='(article, index) in items'>
              <td>{{ article.title }}</td>
              <td>{{ article.category }}</td>
              <td>{{ article.time }}</td>
              <td>{{ article.state }}</td>
              <td>
                  <button>edit</button>
                  <button>delete</button>
              </td>
          </tr>
      </table>
  
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {
                  items: [
                      {
                          title: "做饭指南",
                          category: "美食",
                          time: "2024-03-08",
                          state: "已发布"
                      },
                      {
                          title: "厨房危机",
                          category: "美食",
                          time: "2024-03-08",
                          state: "草稿"
                      },
                      {
                          title: "养生秘诀",
                          category: "美食",
                          time: "2024-03-08",
                          state: "已发布"
                      }
                  ]
              }
          }
      }).mount("#app")
  </script>
  ```

  `v-for`遍历的数据必须在`data`内定义。



##### v-bind

- 作用：动态为 html 标签绑定属性值，如设置 href，src，style 样式等。
- 语法：`v-bind:属性名="属性值"`
- 简写：`:属性名="属性值"`

- 注意：`v-bind`绑定的数据必须在`data`内定义。

- 原始 html

  ```html
  <div id="app">
      <a href="https://bz.zzzmh.cn/index">wallpaper</a>
  </div>
  ```

- 使用`v-bind`

  ```html
  <div id="app">
      <a :href="url">wallpaper</a>
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {
                  url: "https://bz.zzzmh.cn/index"
              }
          }
      }).mount("#app")
  </script>
  ```

  其中`v-bind`可以进一步简写

  ```html
  <a :href="url">wallpaper</a>
  ```



##### v-if & v-show

- 作用：这两类指令，都是用来控制元素的显示与隐藏的
- `v-if`
  - 语法：`v-if="表达式"`，表达式值为 true，显示；flase 隐藏
  - 其它：可以配合`v-else-if/v-else`进行链式调用条件判断
  - 原理：基于条件判断，来控制创建或移除元素节点（条件渲染）
- `v-show`
  - 语法：`v-show="表达式"`，表达式值为 true，显示；false 隐藏
  - 原理：基于 CSS 样式 display 来控制显示与隐藏

- 原始 html

  ```html
  <div id="app">
      衬衫的价格是: <span>9.15</span> <span>12.15</span> <span>22.15</span>
  </div>
  ```

- 使用`v-if`和`v-show`

  ```html
  <div id="app">
      衬衫的价格是: 
      <span v-if="customer.level>=0 && customer.level <=1">9.15</span>
      <span v-else-if="customer.level>=2 && customer.level<=4">12.15</span>
      <span v-else>22.15</span>
  
      <br>
      衬衫的价格是: 
      <span v-show="customer.level>=0 && customer.level <=1">9.15</span>
      <span v-show="customer.level>=2 && customer.level<=4">12.15</span>
      <span v-show="customer.level>=5">22.15</span>
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {
                  customer: {
                      name: "black",
                      level: 2
                  }
              }
          }
      }).mount("#app")
  </script>
  ```

- 使用场景：从性能上看，`v-if`一般适用于不频繁切换显示和隐藏的场景，`v-show`一般适用于频繁切换显示和隐藏的场景。



##### v-on

- 作用：为 html 标签绑定事件

- 语法

  - `v-on:事件名="函数名"`
  - 简写为`@事件名="函数名"`

- 函数必须定义在一个名为`methods`的选项中，该选项与`data`是同级的。

  `createApp({ data(){ 具体的数据 }, methods: { 具体的函数 } })`

- 使用`v-on`

  ```html
  <div id="app">
      <button v-on:click="message">click me</button> &nbsp;
      <button @click="doge">don't click me</button>
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {
  
              }
          },
          methods: {
              message: function() {
                  alert("hello vue3")
              },
              doge: function() {
                  alert("doge")
              }
          }
      }).mount("#app")
  </script>
  ```



##### v-model

- 作用：在表单元素上使用，双向数据绑定。可以方便地获取或设置表单项数据

- 语法：`v-model="变量名"`

- 注意：`v-model`中绑定的变量，必须在`data`中定义。

- 使用`v-model`

  ```html
  <div id="app">
      文章分类：<input type="text" v-model="searchConditions.category"> <span>{{ searchConditions.category }}</span>
      发布状态：<input type="text" v-model="searchConditions.state"> <span>{{ searchConditions.state }}</span>
      <button>搜索</button>
      <button @click="reset">重置</button>
      <br>
      <br>
      <table border="1 solid" colspa="0" cellspacing="0">
          <tr>
              <th>文章标题</th>
              <th>分类</th>
              <th>发布时间</th>
              <th>状态</th>
              <th>操作</th>
          </tr>
          <tr v-for="(article,index) in articleList">
              <td>{{ article.title }}</td>
              <td>{{ article.category }}</td>
              <td>{{ article.time }}</td>
              <td>{{ article.state }}</td>
              <td>
                  <button>编辑</button>
                  <button>删除</button>
              </td>
          </tr>
      </table>
  </div>
  
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {
                  searchConditions: {
                      category: "",
                      state: ""
                  },
                  articleList: [
                      {
                          title: "做饭指南",
                          category: "美食",
                          time: "2024-03-08",
                          state: "已发布"
                      },
                      {
                          title: "厨房危机",
                          category: "美食",
                          time: "2024-03-08",
                          state: "草稿"
                      },
                      {
                          title: "养生秘诀",
                          category: "美食",
                          time: "2024-03-08",
                          state: "已发布"
                      }
                  ]
              }
          },
          methods: {
              reset: function() {
                  // 清空 category 以及 state 的数据
                  // 在 methods 对应的函数里面，使用 this 就代表的是 vue 实例
                  // 可以使用 this 获取到 vue 实例中 data 的数据
                  this.searchConditions.category='';
                  this.searchConditions.state=''; 
              }
          }
      }).mount("#app")
  </script>
  ```

  数据和视图双向变化。



#### 生命周期

- 生命周期：指一个对象从创建到销毁的整个过程。

- 生命周期的八个阶段：每个阶段会自动执行一个生命周期方法(钩子函数)，让开发者有机会在特定的阶段执行自己的代码。

  <img src="https://cn.vuejs.org/assets/lifecycle_zh-CN.FtDDVyNA.png" style="zoom:50%;" >

  |     状态      |  阶段周期  |
  | :-----------: | :--------: |
  | beforeCreate  |   创建前   |
  |    created    |   创建后   |
  |  beforeMount  |   载入前   |
  |    mounted    |  挂载完成  |
  | beforeUpdate  | 数据更新前 |
  |    updated    | 数据更新后 |
  | beforeUnmount | 组件销毁前 |
  |   unmounted   | 组件销毁后 |

- 使用`mounted`钩子

  ```html
  <script type="module">
      import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
      createApp({
          data() {
              return {}
          },
          methods: {},
  
          // mounted: function() {
          //     console.log("挂载完毕");
          // }
          mounted() {
              console.log("挂载完毕");
          }
      }).mount("#app")
  </script>
  ```

- 应用场景：在页面加载完毕时，发起异步请求，加载数据，渲染页面。



#### Axios

- 介绍：Axios 对原生的 Ajax 进行了封装，简化书写，快速开发。
- 官网：https://www.axios-http.cn/

##### Axios 使用步骤

- 引入 Axios 的 js 文件

  ```html
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
  ```

  

- 使用 Axios 发送请求，并获取相应结果

  - method：请求方式，GET/POST....
  - url：请求路径
  - data：请求数据

  ```html
  <!-- 引入 axios 的 js 文件 -->
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
  <script>
      // 发送请求
      /* axios({
          method: "get",
          url: 'http://127.0.0.1:4523/m1/4126110-0-default/article'
      }).then(result => {
          // 成功的回调
          // result 代表服务器响应的所有数据，包含了响应头、响应体
          // result.data 代表的是接口响应的核心数据
          console.log(result.data);
      }).catch(err => {
          // 失败的回调
          console.log(err)
      }); */
  
      let article = {
          title: "做饭指南",
          category: "美食",
          time: "2024-03-08",
          state: "已发布"
      }
      axios({
          method: "post",
          url: 'http://127.0.0.1:4523/m1/4126110-0-default/article',
          data: article
      }).then(result => {
          // 成功的回调
          // result 代表服务器响应的所有数据，包含了响应头、响应体
          // result.data 代表的是接口响应的核心数据
          console.log(result.data);
      }).catch(err => {
          // 失败的回调
          console.log(err)
      });
  </script>
  ```



##### Axios 请求方式别名

- 为了方便，Axios 已经为所有支持的请求方法提供了别名

- 格式：`axios.请求方式(url [, data [, config]])`

- 使用 Axios 别名请求方式

  ```html
  <!-- 引入 axios 的 js 文件 -->
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
  <script>
      let article = {
          title: "做饭指南",
          category: "美食",
          time: "2024-03-08",
          state: "已发布"
      }
  
      // 别名方式发送请求
      /* axios.get('http://127.0.0.1:4523/m1/4126110-0-default/article').then(result => {
                  // 成功的回调
                  // result 代表服务器响应的所有数据，包含了响应头、响应体
                  // result.data 代表的是接口响应的核心数据
                  console.log(result.data);
              }).catch(err => {
                  // 失败的回调
                  console.log(err)
              }); */
  
      axios.post('http://127.0.0.1:4523/m1/4126110-0-default/article', article).then(result => {
          // 成功的回调
          // result 代表服务器响应的所有数据，包含了响应头、响应体
          // result.data 代表的是接口响应的核心数据
          console.log(result.data);
      }).catch(err => {
          // 失败的回调
          console.log(err)
      });
  </script>
  ```



#### Vue 案例

使用表格展示所有文章的数据，并完成条件搜索功能

- 钩子函数`mounted`中，获取所有的文章数据

  引入 vue 模块、axios 模块，在 mounted 钩子中使用 axios 请求数据，将数据赋值给定义在 data 中的键。

- 使用`v-for`指令，把数据渲染到表格上展示

  遍历 data 中表格对应键的数组数据。

- 使用`v-model`指令完成表单数据的双向绑定

  在 data 中定义对应的键，并与输入框双向绑定。

- 使用`v-on`指令为搜索按钮绑定单击事件

  在搜索按钮上绑定事件发送 axios 请求，携带双向绑定的数据作为搜索条件，将得到的数据再赋值给表格对应的键，更新数据。 

参考实现

```html
<div id="app">

    文章分类：<input type="text" v-model="searchConditions.category">

    发布状态：<input type="text" v-model="searchConditions.state">

    <button @click="search">搜索</button>

    <br>
    <br>
    <table border="1 solid" colspa="0" cellspacing="0">
        <tr>
            <th>文章标题</th>
            <th>分类</th>
            <th>发布时间</th>
            <th>状态</th>
            <th>操作</th>
        </tr>

        <tr v-for="(article,index) in articleList">
            <td>{{ article.title }}</td>
            <td>{{ article.category }}</td>
            <td>{{ article.time }}</td>
            <td>{{ article.state }}</td>
            <td>
                <button>编辑</button>
                <button>删除</button>
            </td>
        </tr>
    </table>
</div>

<!-- 引入 axios 的 js 文件 -->
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script type="module">
    // 导入 vue 模块
    import { createApp } from "https://unpkg.com/vue@3.4.19/dist/vue.esm-browser.js"
    // 创建 vue 实例
    createApp({
        data() {
            return {
                articleList: [],
                searchConditions: {
                    category: "",
                    state: ""
                },
            }
        },
        methods: {
            // 声明函数
            search: function() {
                // 发送请求，完成搜索，携带搜索条件
                axios.get('http://127.0.0.1:4523/m1/4126110-0-default/search?category=' + this.searchConditions.category + 
                          '&state=' + this.searchConditions.state).then(result => {
                    // console.log(result.data);
                    // 成功回调 result.data
                    // 把 result.data 的数据赋值给 articleList
                    this.articleList=result.data;
                }).catch(err => {
                    console.log(err);
                })
            }
        },
        // 钩子函数 mounted 中，获取所有的文章数据
        mounted() { 
            // 发送异步请求 axios
            axios.get('http://127.0.0.1:4523/m1/4126110-0-default/article').then(result => {
                // 成功回调
                // console.log(result.data);
                this.articleList=result.data;
            }).catch(err => {
                // 失败回调
                console.log(err);
            })
        }
    }).mount("#app")
</script>
```





### 整站使用 Vue (工程化)

#### 环境准备

- 介绍：create-vue 是 Vue 官方提供的最新脚手架工具，用于快速生成一个工程化的 Vue 项目。
- create-vue 提供了如下功能：
  - 统一的项目结构
  - 本地调试
  - 热部署
  - 单元测试
  - 集成打包
- 依赖环境：NodeJS
- npm：Node Package Manager，是 NodeJS 的软件包管理器。

#### Vue 项目创建与启动

##### Vue 项目创建

- 创建一个工程化的 Vue 项目，执行命令

  ```bash
  pnpm create vue@latest
  ```

##### Vue 项目目录结构

- vite.config.js：Vue 项目的配置信息，如端口号等
- package.json：项目配置文件，包括项目名、版本号、依赖包及其版本等
- package-lock.json：项目配置文件，自动生成
- index.html：默认首页
- public：公共资源
- node_modules：下载的第三方包存放目录
- src：源代码存放目录
  - assets：静态资源目录，存放图片、字体....
  - components：组件目录，存放通用组件
  - App.vue：根组件
  - main.js：入口文件


##### Vue 项目启动

- 执行命令

  ```bash
  pnpm dev
  ```

  即可启动 Vue 项目。

- 访问项目：打开浏览器，访问控制台显示的 url 地址。



#### Vue 项目开发流程

index.html 是默认的首页，在 index.html 中引入了 main.js 文件，而在 main.js 中又引入了 App.vue 文件，同时引入了 Vue 模块，创建了 Vue 实例，把 App.vue 挂载到了 index.html 上。因此，后续开发的重点是围绕 App.vue 的。

*.vue 是 Vue 项目中的组件文件，也成为单文件组件 (SFC, Single-File Components)。Vue 的单文件组件会将一个组件的逻辑 (JS) , 模板 (HTML) 和样式 (CSS) 封装在同一个文件里 (\*.vue)

组件编写涉及到两种不同风格的 API 写法：选项式 API 和组合式 API。这里先大致看一下两种不同风格的代码。

选项式 API 风格的代码如下

```vue
<script>
export default{
  data() {
    return {
      msg: 'Hello Vue'
    }
  }
}
</script>

<template>
  <h1>{{ msg }}</h1>
</template>

<style scoped>
h1{
  color: green;
}
</style>
```

组合式 API 风格的代码如下

```vue
<script setup>
import { ref } from "vue";
// 调用 ref 函数，定义响应式数据
const msg = ref('Hello Vue');
</script>

<template>
  <h1>{{ msg }}</h1>
</template>

<style scoped>
h1 {
  color: green;
}
</style>
```

在`script`标签处需要定义`setup`的属性，然后才能编写组合式 API。在组合式 API 中使用响应式数据需要借助`ref`函数创建。



#### API 风格

- Vue 的组件有两种不同的风格：组合式 API 和 选项式 API
- 选项式 API 可以用包含多个选项的对象来描述组件的逻辑，如 data，methods，mounted 等，缺点是格式比较死板，因此还有组合式 API 的选择。
- 组合式 API 则相对比较灵活
  - 在组合式 API 中使用类似于选项式 API 的 data 响应式数据，可以借助`ref`函数
  - 声明普通函数可以像原生 JS 一样直接声明
  - 声明钩子函数可以借助 Vue 模块提供的`onMounted`函数。
- 组合式 API 因为其灵活性，在大项目中比较推荐使用。



##### 组合式 API

编写一个新的组件如下

```vue
<script setup>
import { onMounted, ref } from 'vue'
const count = ref(0); // 声明响应式变量
function increment() { // 定义函数
  count.value++;
}
onMounted(() => { // 声明钩子函数
  console.log('mounted!')
})
</script>
<template>
    <button @click="increment">count: {{ count }}</button>
</template>
```

- `setup`: 是一个标识，告诉 Vue 需要进行一些处理，让我们可以更简洁地使用组合式 API。是一个语法糖，原始使用组合式 API 的写法稍微复杂一点。
- `ref()`: 接收一个内部值，返回一个响应式 ref 对象，此对象只有一个指向内部的值 value。可以通过 value 访问该响应式对象所封装的值。
- `onMounted()`: 是组合式 API 中的钩子函数，注册一个回调函数，在组件挂载完成后执行。

组件编写完成后需要在 App.vue 中导入并使用

```vue
<script setup>
import { ref } from "vue";
// 调用 ref 函数，定义响应式数据
const msg = ref('Hello Vue');

// 导入 Api.vue 文件
import Api from './Api.vue';
</script>

<template>
  <h1>{{ msg }}</h1>
  <br>
  <Api/>
</template>
```



#### 案例

以组合式 API 的方式实现前面的案例

> 使用表格展示所有文章的数据，并完成条件搜索功能
>
> - 钩子函数`mounted`中，获取所有的文章数据
>
>   引入 vue 模块、axios 模块，在 mounted 钩子中使用 axios 请求数据，将数据赋值给定义在 data 中的键。
>
> - 使用`v-for`指令，把数据渲染到表格上展示
>
>   遍历 data 中表格对应键的数组数据。
>
> - 使用`v-model`指令完成表单数据的双向绑定
>
>   在 data 中定义对应的键，并与输入框双向绑定。
>
> - 使用`v-on`指令为搜索按钮绑定单击事件
>
>   在搜索按钮上绑定事件发送 axios 请求，携带双向绑定的数据作为搜索条件，将得到的数据再赋值给表格对应的键，更新数据。 

这里要使用 Axios 库可以直接安装到本地

```bash
pnpm add axios
```

参考实现

```vue
<script setup>
// 导入 axios 
import axios from "axios";
import { onMounted,ref } from "vue";
// 定义文章列表响应式数据
const articleList = ref([]);
// 发送异步请求获取文章列表数据
// onMounted(() => {
    axios.get('http://127.0.0.1:4523/m1/4126110-0-default/article').then(result => {
        // console.log(result.data);
        articleList.value = result.data;
    }).catch(err => {
        console.log(err);
    })
// })
// 定义搜索条件响应式数据
const searchConditions = ref({
    category: '',
    state: ''
})
// 定义 search 函数
/* function search() {
    axios.get('http://127.0.0.1:4523/m1/4126110-0-default/search?category='+searchConditions.value.category+
    '&state='+searchConditions.value.state).then(result => {
        // console.log(result.data);
        articleList.value = result.data;
    }).catch(err => {
        console.log(err);
    })
} */
// 另一种写法
const search = () => {
    axios.get('http://127.0.0.1:4523/m1/4126110-0-default/search', {params: {...searchConditions.value}}).then(result => {
        articleList.value = result.data;
    }).catch(err => {
        console.log(err);
    })
}
</script>

<template>
    文章分类：<input type="text" v-model="searchConditions.category">
    发布状态：<input type="text" v-model="searchConditions.state">
    <button @click="search">搜索</button>
    <table border="1 solid" colspa="0" cellspacing="0">
        <tr>
            <th>文章标题</th>
            <th>分类</th>
            <th>发布时间</th>
            <th>状态</th>
            <th>操作</th>
        </tr>

        <tr v-for="(article,index) in articleList">
            <td>{{ article.title }}</td>
            <td>{{ article.category }}</td>
            <td>{{ article.time }}</td>
            <td>{{ article.state }}</td>
            <td>
                <button>编辑</button>
                <button>删除</button>
            </td>
        </tr>

    </table>
</template>
```

> 注意`value`的使用

这里可以不使用`onMounted`钩子函数，代码在解析的时候会自动从上往下执行，因此这里获取文章列表数据的 Axios 请求会自动执行。

`{...searchConditions.value}`是一种解构写法，会自动解析`searchConditions`中的数据。

##### 代码优化(初步)

前面的代码在工程实践中并不规范，接口请求的代码都写在一个组件中，难以复用。

- 接口调用的 js 代码一般会封装到 .js 文件中，并且以函数的形式暴露给外部

  在`src`目录下创建一个`api`目录，将前面的 axios 代码提取出来，放到`api`目录下的`article.js`中

  ```js
  // 导入 axios 
  import axios from "axios";
  
  // 获取所有文章数据
  export function articleListService() {
      axios.get('http://127.0.0.1:4523/m1/4126110-0-default/article')
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  
  // 根据文章分类和发布状态搜索数据
  export function articleSearchService(conditions) {
      axios.get('http://127.0.0.1:4523/m1/4126110-0-default/search', { params: conditions })
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  ```

  需要注意，由于将函数提取出来之后就无法直接访问原来的响应式数据，这里对于给响应式数据赋值可以使用`return`返回数据，对于需要携带响应式数据发起请求可以使用函数参数将响应式数据传递给函数。

  将`article.js`导入到原来的 vue 组件中使用

  ```vue
  <script setup>
  import { articleListService,articleSearchService } from '@/api/article.js'
  import { onMounted,ref } from "vue";
  // 定义文章列表响应式数据
  const articleList = ref([]);
  // 获取文章列表数据
  let data = articleListService();
  articleList.value = data;
  // 定义搜索条件响应式数据
  const searchConditions = ref({
      category: '',
      state: ''
  })
  // 定义搜索函数
  const search = () => {
      // 文章搜索
      let data = articleSearchService({...searchConditions.value});
      articleList.value = data;
  }
  </script>
  
  <template>
      文章分类：<input type="text" v-model="searchConditions.category">
      发布状态：<input type="text" v-model="searchConditions.state">
      <button @click="search">搜索</button>
      <table border="1 solid" colspa="0" cellspacing="0">
          <tr>
              <th>文章标题</th>
              <th>分类</th>
              <th>发布时间</th>
              <th>状态</th>
              <th>操作</th>
          </tr>
  
          <tr v-for="(article,index) in articleList">
              <td>{{ article.title }}</td>
              <td>{{ article.category }}</td>
              <td>{{ article.time }}</td>
              <td>{{ article.state }}</td>
              <td>
                  <button>编辑</button>
                  <button>删除</button>
              </td>
          </tr>
  
      </table>
  </template>
  ```

  这时启动项目会发现并没有获取到数据，这是因为 axios 请求是异步的，在调用函数给`articleList.value`赋值时，不知道什么时候能够返回服务器的数据。因而，这里的赋值会因为等不到数据，提前渲染了没有数据的页面。

  这就需要对 axios 请求进行同步等待服务器响应结果的处理，涉及到`async`和`await`关键词的使用。

  ```js
  // 获取所有文章数据
  export async function articleListService() {
      // 同步等待服务器响应的结果，并返回给调用者
      return await axios.get('http://127.0.0.1:4523/m1/4126110-0-default/article')
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  ```

  > 这里如果请求很简单，其实可以省略`async/await`的编写，直接返回 axios 实例。如果涉及比较复杂的多个组合请求，上下文之间存在依赖关系，则需要编写`async/await`。

  不过此时还无法获取`articleListService`返回的数据，需要在组件里同步获取函数的返回结果。

  由于`async`和`await`是绑定使用的，这里还需要把同步获取函数返回结果的操作封装成函数，并且该函数不会自动执行，还需要手动调用。

  ```vue
  const getArticleList = async () => {
      let data = await articleListService();
      articleList.value = data;
  }
  // 需要手动调用函数获取数据
  getArticleList();
  ```

  同理改造另一个函数

  js 部分

  ```js
  // 根据文章分类和发布状态搜索数据
  export async function articleSearchService(conditions) {
      return await axios.get('http://127.0.0.1:4523/m1/4126110-0-default/search', { params: conditions })
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  ```

  vue 部分

  ```vue
  // 定义搜索函数
  const search = async () => {
      // 文章搜索
      let data = await articleSearchService({...searchConditions.value});
      articleList.value = data;
  }
  ```

- 在前面的接口请求代码中可以发现，请求的 url 存在共同的前缀。当项目存在大量的共同前缀的请求地址时，可以将共同的前缀提取出来。

  定义一个`baseURL`记录共同的前缀，再使用 axios 的`create`方法创建一个基于`baseURL`的请求实例，后续的请求都是基于此实例，这就解决了共同前缀重复编写的问题。

  ```js
  // 导入 axios 
  import axios from "axios";
  
  // 定义公共前缀变量 baseURL
  const baseURL = 'http://127.0.0.1:4523/m1/4126110-0-default';
  const instance = axios.create({ baseURL });
  
  // 获取所有文章数据
  export async function articleListService() {
      // 同步等待服务器响应的结果，并返回给调用者
      return await instance.get('/article')
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  
  // 根据文章分类和发布状态搜索数据
  export async function articleSearchService(conditions) {
      return await instance.get('/search', { params: conditions })
      .then(result => {
          return result.data;
      }).catch(err => {
          console.log(err);
      })
  }
  ```



代码优化小结：

- axios 代码提取单独封装成 js 文件，使用`async/await`同步获取数据
- 公共前缀优化



##### 代码优化(进阶)

在前面的 js 代码中，`.then().catch()`结构的代码重复出现。在实践中可以提供一个`request.js`作为请求工具，在这个请求工具中定制请求实例，再暴露给外部使用。此时需要使用 axios 提供的拦截器。

拦截器：在请求或响应被`then`或`catch`处理前拦截它们，分为请求拦截器和响应拦截器。

请求拦截器：如果请求成功，可以配置统一的请求头，比如 token；请求失败则可以进行相应的错误处理。

响应拦截器：如果响应成功，则对响应的数据做统一处理；响应失败则给出统一的错误提示。

这里以响应拦截器为例，封装的`requset.js`如下

```js
// 定制请求实例

// 导入 axios 
import axios from "axios";
// 定义公共前缀变量 baseURL
const baseURL = 'http://127.0.0.1:4523/m1/4126110-0-default';
const instance = axios.create({ baseURL });

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        return result.data;
    },
    err => {
        alert('请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;
```

原来的`article.js`修改如下

```js
/* // 导入 axios 
import axios from "axios";
// 定义公共前缀变量 baseURL
const baseURL = 'http://127.0.0.1:4523/m1/4126110-0-default';
const instance = axios.create({ baseURL }); */

import request from "@/util/request.js";

// 获取所有文章数据
export async function articleListService() {
    // 同步等待服务器响应的结果，并返回给调用者
    return await request.get('/article');
}

// 根据文章分类和发布状态搜索数据
export async function articleSearchService(conditions) {
    return await request.get('/search', { params: conditions });
}
```

这里还可以省略`async/await`的编写，调用函数的部分依旧保留`async/await`。

```js
import request from "@/util/request.js";

// 获取所有文章数据
export function articleListService() {
    // 同步等待服务器响应的结果，并返回给调用者
    return request.get('/article');
}

// 根据文章分类和发布状态搜索数据
export function articleSearchService(conditions) {
    return request.get('/search', { params: conditions });
}
```



> 关于请求拦截器的配置会在后续使用的时候说明



### Element-Plus

- Element-Plus：是饿了么团队研发的、基于 Vue3、面向设计师和开发者的组件库。
- 组件：组成网页的部件，例如 超链接、按钮、图片、表格、表单、分页条等。
- 官网：https://element-plus.org/zh-CN/#/zh-CN

#### 快速入门

准备工作

1. 创建一个工程化的 vue 项目

2. 参考官方文档安装 Element Plus 组件库

   ```bash
   pnpm install element-plus @element-plus/icons-vue
   ```

3. `main.js`中引入 Element Plus 组件库

   ```js
   import { createApp } from 'vue' // 导入 vue
   import ElementPlus from 'element-plus' // 导入 element-plus
   import 'element-plus/dist/index.css' // 导入 element-plus 的样式
   import App from './App.vue' // 导入 App.vue
   
   const app = createApp(App) // 创建一个 vue 实例
   
   app.use(ElementPlus) // 使用 element-plus
   app.mount('#app') // 挂载到 id 为 app 的元素上
   ```

   

制作组件

将 Element Plus 官网的组件代码复制到自定义的组件中再导入到根组件，启动项目即可预览效果，后续修改只需要对已有的代码参考官方文档进行调整。



#### 常用组件

组件可以先参考现有的原型，然后利用组件库调整设计出合适的自定义组件。

表格组件，参考示例

```vue
<script lang="ts" setup>
import {
  Delete,
  Edit
} from "@element-plus/icons-vue";
const articleList = [
  {
    title: "做饭指南",
    category: "美食",
    time: "2024-03-08",
    state: "已发布",
  },
  {
    title: "厨房危机",
    category: "美食",
    time: "2024-03-08",
    state: "草稿",
  },
  {
    title: "养生秘诀",
    category: "美食",
    time: "2024-03-08",
    state: "已发布",
  },
];
</script>

<template>
  <el-table :data="articleList" style="width: 100%">
    <el-table-column prop="title" label="文章标题" />
    <el-table-column prop="category" label="分类" />
    <el-table-column prop="time" label="发布时间" />
    <el-table-column prop="state" label="状态" />
    <el-table-column label="操作" width="180">
      <div>
        <el-button type="primary" :icon="Edit" circle />
        <el-button type="danger" :icon="Delete" circle />
      </div>
    </el-table-column>
  </el-table>
</template>
```

分页条，参考示例

```vue
<script lang="ts" setup>

import { ref } from "vue";

const currentPage4 = ref(4);
const pageSize4 = ref(5);
const small = ref(false);
const background = ref(false);
const disabled = ref(false);
const total = ref(20);

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
};
    
import { Delete, Edit } from "@element-plus/icons-vue";

const articleList = [/*...*/];
</script>

<template>
  <el-table :data="articleList" style="width: 100%">
    <!-- .... -->
  </el-table>

  <el-pagination
    class="el-p"
    v-model:current-page="currentPage4"
    v-model:page-size="pageSize4"
    :page-sizes="[5, 10, 15, 20]"
    :small="small"
    :disabled="disabled"
    :background="background"
    layout="jumper, total, sizes, prev, pager, next"
    :total="total"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />
</template>

<style scoped>
.el-p {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
```

表单组件，参考示例

```vue
<script lang="ts" setup>
import { reactive } from 'vue'

const formInline = reactive({
  category: '',
  state: ''
})

const onSubmit = () => {
  console.log('submit!')
}

import { ref } from "vue";

const currentPage4 = ref(4);
const pageSize4 = ref(5);
const small = ref(false);
const background = ref(false);
const disabled = ref(false);
const total = ref(20);

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
};

import { Delete, Edit } from "@element-plus/icons-vue";

const articleList = [ /*...*/ ];
</script>

<template>
  <el-form :inline="true" :model="formInline" class="demo-form-inline">
    
    <el-form-item label="文章分类：">
      <el-select
        v-model="formInline.category"
        placeholder="请选择"
        clearable
      >
        <el-option label="美食" value="美食" />
        <el-option label="前端" value="前端" />
      </el-select>
    </el-form-item>

    <el-form-item label="发布状态：">
      <el-select
        v-model="formInline.state"
        placeholder="请选择"
        clearable
      >
        <el-option label="已发布" value="已发布" />
        <el-option label="草稿" value="草稿" />
      </el-select>
    </el-form-item>
   
    <el-form-item>
      <el-button type="primary" @click="onSubmit">搜索</el-button>
    </el-form-item>
    <el-form-item>
      <el-button type="default" @click="onSubmit">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="articleList" style="width: 100%">
    <!-- .... -->
  </el-table>

  <el-pagination
    class="el-p"
    v-model:current-page="currentPage4"
    v-model:page-size="pageSize4"
    :page-sizes="[5, 10, 15, 20]"
    :small="small"
    :disabled="disabled"
    :background="background"
    layout="jumper, total, sizes, prev, pager, next"
    :total="total"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />
</template>

<style scoped>
.el-p {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>
```

卡片组件，参考示例

```vue
<script lang="ts" setup>
import { reactive } from 'vue'

const formInline = reactive({
  category: '',
  state: ''
})

const onSubmit = () => {
  console.log('submit!')
}

import { ref } from "vue";

const currentPage4 = ref(4);
const pageSize4 = ref(5);
const small = ref(false);
const background = ref(false);
const disabled = ref(false);
const total = ref(20);

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
};

import { Delete, Edit } from "@element-plus/icons-vue";

const articleList = [ /*...*/ ];
</script>

<template>

  <el-card>
    <div class="card-header">
      <span>文章管理</span>
      <el-button type="primary">发布文章</el-button>
    </div>

    <div style="margin-top: 20px;">
      <hr>
    </div>

    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <!-- .... -->
    </el-form>
    <el-table :data="articleList" style="width: 100%">
      <!-- .... -->
    </el-table>

    <el-pagination class="el-p" v-model:current-page="currentPage4" v-model:page-size="pageSize4"
      :page-sizes="[5, 10, 15, 20]" :small="small" :disabled="disabled" :background="background"
      layout="jumper, total, sizes, prev, pager, next" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </el-card>
</template>

<style scoped>
.el-p {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}

.card-header {
  display: flex;
  justify-content: space-between;
}
</style>
```



### 大事记前端

#### 概述

- 基本页面：登录页面、注册页面

- 首页结构

  - 左侧导航栏，包括文章分类、文章管理、个人中心。个人中心还有三个子菜单，分别为基本资料、更换头像、重置密码。

  - 右侧分为顶部、中部、底部三部分。顶部和底部是固定的，中部会根据用户点击侧边导航栏的不同菜单进行切换。首次登录默认是展示文章管理的内容。

    文章分类和文章管理页面都具有增删改查的功能，文章管理页面还提供了分页和条件搜索的功能。

  - 用户相关功能，在右侧顶部展示当前用户的昵称和头像。在左侧的个人中心可以修改个人资料、更换头像、重置密码。

需求

- 用户
  - 注册
  - 登录
  - 获取用户详细信息
  - 更新用户基本信息
  - 更新用户头像
  - 更新用户密码
- 文章分类
  - 文章列表分类
  - 新增文章分类
  - 更新文章分类
  - 删除文章分类
- 文章管理
  - 新增文章
  - 更新文章
  - 删除文章
  - 文章列表(条件分页)

#### 准备工作

1. 创建 Vue 工程

   ```bash
   pnpm create vue@latest
   ```

2. 安装依赖

   Element-Plus 组件库及其图标库、Axios 库、Sass (CSS 语言扩展包)

   ```bash
   pnpm add element-plus @element-plus/icons-vue
   pnpm add axios
   pnpm add sass -D
   ```

3. 目录调整

   - 删除 components 下面自动生成的内容
   - 新建目录 api、utils、views
   - 将新资料中的静态资源拷贝到 assets 目录下
   - 删除 App.vue 中自动生成的内容



#### 注册

开发步骤：

- 搭建页面：html 标签、css样式

  > 技巧：登录和注册表单可以写在同一个文件里，配合`v-if`指令使用，可以在同一个页面内切换登录和注册的表单。需要定义一个响应式变量并通过按钮进行控制。

  ```vue
  <script lang="ts" setup>
  import { User,Lock } from '@element-plus/icons-vue'
  import { ref, reactive } from 'vue'
  // 控制注册与登录表单的显示，默认显示登录表单
  const isRegister = ref(false)
  
  </script>
  
  <template>
      <el-row class="login-page">
          <el-col :span="12" class="bg"></el-col>
          <el-col :span="6" :offset="3" class="form">
              <!-- 注册表单 -->
              <el-form :model="registerData" label-width="auto" style="max-width: 600px" v-if="isRegister" :rules="rules">
                  <el-form-item>
                      <h1>注册</h1>
                  </el-form-item>
                  <el-form-item prop="username">
                      <el-input :prefix-icon="User" v-model="registerData.username" placeholder="请输入用户名" />
                  </el-form-item>
                  <el-form-item prop="password">
                      <el-input :prefix-icon="Lock" v-model="registerData.password" type="password" placeholder="请输入密码" />
                  </el-form-item>
                  <el-form-item prop="rePassword">
                      <el-input :prefix-icon="Lock" v-model="registerData.rePassword" type="password" placeholder="请再次输入密码" />
                  </el-form-item>
                  <!-- 注册按钮 -->
                  <el-form-item>
                      <el-button class="button" type="primary" @click="register" auto-insert-space>
                          注册
                      </el-button>
                  </el-form-item>
                  <el-form-item class="flex">
                      <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                          返回
                      </el-link>
                  </el-form-item>
              </el-form>
  
              <!-- 登录表单 -->
              <el-form :model="registerData" label-width="auto" style="max-width: 600px" v-else :rules="rules" >
                  <el-form-item>
                      <h1>登录</h1>
                  </el-form-item>
                  <el-form-item prop="username">
                      <el-input :prefix-icon="User" v-model="registerData.username" placeholder="请输入用户名" />
                  </el-form-item>
                  <el-form-item prop="password">
                      <el-input :prefix-icon="Lock" v-model="registerData.password" type="password" placeholder="请输入密码" />
                  </el-form-item>
                  <el-form-item class="flex">
                      <div class="flex">
                          <el-checkbox  label="Option 1" size="large" >记住我</el-checkbox>
                          <el-link type="primary" :underline="false">忘记密码？</el-link>
                      </div>
                  </el-form-item>
                  <!-- 登录按钮 -->
                  <el-form-item>
                      <el-button class="button" type="primary" @click="login" auto-insert-space >登录</el-button>
                  </el-form-item>
                  <el-form-item class="flex">
                      <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                          注册
                      </el-link>
                  </el-form-item>
              </el-form>
          </el-col>
      </el-row>
  </template>
  
  <style lang="scss" scoped>
  /* 样式 */
  .login-page {
      height: 100vh;
      background-color: #fff;
  
      .bg {
          background: url('@/assets/login-bg.jpg') no-repeat center / cover;
          border-radius: 0 20px 20px 0;
      }
  
      .form {
          display: flex;
          flex-direction: column;
          justify-content: center;
          user-select: none;
  
          .title {
              margin: 0 auto;
          }
  
          .button {
              width: 100%;
          }
  
          .flex {
              width: 100%;
              display: flex;
              justify-content: space-between;
          }
      }
  }
  </style>
  ```

- 绑定数据与事件：表单校验

  根据接口文档和 element-plus 的文档编写

  ```vue
  <script lang="ts" setup>
  import { User,Lock } from '@element-plus/icons-vue'
  import { ref, reactive } from 'vue'
  // 控制注册与登录表单的显示，默认显示登录表单
  const isRegister = ref(false)
  // 数据模型
  const registerData = reactive({
      username: '',
      password: '',
      rePassword: ''
  })
  
  // 校验密码是否一致
  const checkRePassward = (rule, value, callback) => {
      if (value==='') {
          callback(new Error('请再次确认密码'))
      } else if (value !== registerData.password) {
          callback(new Error('两次输入密码不一致'))
      } else {
          callback()
      }
  }
  
  // 表单校验规则
  const rules = {
      username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
      ],
      password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
      ],
      rePassword: [
          { validator: checkRePassward, trigger: 'blur' }
      ]
  }
  </script>
  ```

  关于`ref`和`reactive`

  ref

  - **场景**：当你需要包装一个基础类型（如 `string`、`number`、`boolean` 等）或需要替换引用类型的整个值（如整个对象或数组）时，使用 `ref`。
  - **特点**：`ref` 会自动将内部的值变为响应式的。使用 `ref` 定义的引用类型或基础类型，在模板中直接使用时无需 `.value`，但在 JavaScript 中操作其值需要使用 `.value`。

  reactive

  - **场景**：当你需要创建一个响应式的复杂对象（如嵌套对象）时，使用 `reactive`。
  - **特点**：`reactive` 提供深层次的响应式转换，不需要通过 `.value` 访问或修改对象的属性。它更适合用于复杂的数据结构。
  - 但是`reactive`无法像`ref`一样**直接替换数据**。使用 `ref`，你可以在获取到新的数据后，直接替换 `ref` 包含的值。这对于异步操作如从 API 获取数据尤其有用，因为你通常会替换整个数据集。

- 调用后台接口：接口文档、api 封装、页面函数调用

  启动后端项目`java -jar *.jar`，根据接口文档编写请求函数，这里需要注意跨域问题
  
  user.js
  
  ```js
  // 导入 request.js 请求工具
  import request from '@/utils/request.js'
  
  // 注册接口
  export const userRegisterService = (registerData) => {
      // 借助 urlSearchParams 对象将对象转换为字符串
      const params = new URLSearchParams()
      for (let key in registerData) {
          params.append(key, registerData[key]);
      }
      return request.post('/user/register', params)
      // 另一种写法
      // return request.post('/user/register', registerData, { headers: { 'Content-Type': 'application/x-www-form-urlencoded' }})
  }
  ```
  
  Login.vue
  
  ```vue
  <script lang="ts" setup>
  import { User,Lock } from '@element-plus/icons-vue'
  import { ref, reactive } from 'vue'
  // 控制注册与登录表单的显示，默认显示登录表单
  const isRegister = ref(false)
  // 数据模型
  const registerData = reactive({
      username: '',
      password: '',
      rePassword: ''
  })
  
  // 校验密码是否一致
  // ....
  
  // 表单校验规则
  // ....
  
  // 调用注册接口
  import { userRegisterService, userLoginService } from '@/api/user'
  const register = async () => {
      let result = await userRegisterService(registerData);
      if (result.code === 0) {
          // 成功
          console.log('注册成功')   
      } else {
          // 失败
          console.log('注册失败')
      }
  }
  </script>
  ```
  
  

#### 跨域

由于浏览器的同源策略限制，向不同源(不同协议、不同域名、不同端口)发送 ajax 请求会失败。

例如在这里前端项目是 http://localhost:5173 , 后端项目是 http://localhost:8080。端口号不同，请求发送会失败，这就是跨域问题。

这可以通过在前端配置代理转发解决：发送异步请求的时候会先发送到前端服务，再由前端服务转发到后端服务，由于浏览器不再直接向后端服务请求，而是请求同源的前端服务，因此不会遇到跨域问题。

> 跨域问题只发生在浏览器中，前端服务和后端服务之间的转发没有跨域问题。
>
> 在后端项目中也可以配置`CORS`用于解决跨域问题，这也是实际生产环境推荐的做法。在前端项目配置跨域只是为了方便在开发环境中测试，在实际生产环境中，前端部署之后是没有自带的代理功能的，代理功能本身就是由 vite 之类的服务提供的。



代理的配置需要在 request.js 和 vite.config.js 中进行代理相关配置。

在 request.js 中修改`baseURL`

```js
// 定制请求实例

// 导入 axios 
import axios from "axios";
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        return result.data;
    },
    err => {
        alert('请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;
```

在 vite.config.js 中配置`proxy`

```js
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': { // 获取路径中包含了 /api 的请求
        target: 'http://localhost:8080', //  转发的目标地址
        changeOrigin: true, // 支持跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 重写路径，将其中的 /api 去掉
      }
    }
  }
})
```



#### 登录

登录的表单和注册的表单大致相同，数据模型可以复用注册的数据模型，表单校验规则也是可复用的。不过由于绑定了同一个数据模型，需要手动调用函数清空已填写的数据。

```vue
<script lang="ts" setup>
import { User,Lock } from '@element-plus/icons-vue'
import { ref, reactive } from 'vue'
// 控制注册与登录表单的显示，默认显示登录表单
const isRegister = ref(false)
// 数据模型
const registerData = reactive({
    username: '',
    password: '',
    rePassword: ''
})

// 校验密码是否一致
// ....

// 表单校验规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassward, trigger: 'blur' }
    ]
}

import { userRegisterService, userLoginService } from '@/api/user'
// 调用注册接口
// ....

// 登录数据模型，复用注册数据模型
// 表单校验规则，复用注册表单校验规则
// 调用登录接口
const login = async () => {
    let result = await userLoginService(registerData)
    if (result.code === 0) {
        console.log('登录成功')
        alert(result.message?result.message:'登录成功')
    } else {
        console.log('登录失败')
        alert(result.message?result.message:'登录失败')
    }
}
// 出于安全考虑，切换登录和注册时需要手动清空数据，也可以考虑使用独立的登录数据模型
const clearRegisterData = () => {
    registerData.username = ''
    registerData.password = ''
    registerData.rePassword = ''
}
</script>
```



#### Axios 拦截器优化

对于登录和注册接口的成功回调和失败回调还可以进一步优化，由拦截器进行统一处理。

修改 request.js

```js
// 定制请求实例

// 导入 axios 
import axios from "axios";
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 判断业务状态码
        if (result.data.code === 0) {
            // 业务成功
            return result.data;
        }

        // 业务失败
        alert(result.data.message?result.data.message:'请求失败，请稍后重试');
        // 将异步状态改为失败状态，方便被 catch 捕获
        return Promise.reject(result.data);
        
    },
    err => {
        alert('请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;
```

优化 Login.vue 的代码

```vue
<script lang="ts" setup>
// ....

import { userRegisterService, userLoginService } from '@/api/user'
// 调用注册接口
const register = async () => {
    let result = await userRegisterService(registerData);
    /* if (result.code === 0) {
        // 成功
        console.log('注册成功')   
    } else {
        // 失败
        console.log('注册失败')
    } */
    alert(result.message?result.message:'注册成功')
}

// 登录数据模型，复用注册数据模型
// 表单校验规则，复用注册表单校验规则
// 调用登录接口
const login = async () => {
    let result = await userLoginService(registerData)
    /* if (result.code === 0) {
        console.log('登录成功')
        alert(result.message?result.message:'登录成功')
    } else {
        console.log('登录失败')
        alert(result.message?result.message:'登录失败')
    } */
    alert(result.message?result.message:'登录成功')
}
</script>
```



#### 错误提示优化

使用 element-plus 的 消息提示 组件代替浏览器自带的弹窗。

在 request.js 配置

```js 
// 定制请求实例

// 导入 axios 
import axios from "axios";

import { ElMessage } from 'element-plus'
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 判断业务状态码
        if (result.data.code === 0) {
            // 业务成功
            return result.data;
        }

        // 业务失败
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        // 将异步状态改为失败状态，方便被 catch 捕获
        return Promise.reject(result.data);
        
    },
    err => {
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;
```

在 Login.vue 配置

```vue
<script lang="ts" setup>
import { ElMessage } from 'element-plus'
// ....

import { userRegisterService, userLoginService } from '@/api/user'
// 调用注册接口
const register = async () => {
    let result = await userRegisterService(registerData);
    ElMessage.success(result.message?result.message:'注册成功')
}

// 登录数据模型，复用注册数据模型
// 表单校验规则，复用注册表单校验规则
// 调用登录接口
const login = async () => {
    let result = await userLoginService(registerData)
    ElMessage.success(result.message?result.message:'登录成功')
}
</script>
```



#### 主页面搭建

编写主页面组件，然后在根组件中引入使用。为了方便观察页面，可以先在根组件注释掉登录注册页面的组件

主页面参考

```vue
<script setup>
import {
    CaretBottom,
    SwitchButton,
    EditPen,
    Crop,
    User,
    UserFilled,
    Promotion,
    Management,
} from '@element-plus/icons-vue'
import avatar from '@/assets/avatar.webp'
</script>

<template>
    <!-- element-plus 容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- element-plus 菜单 -->
            <el-menu active-text-color="#ffd04b" background-color="#232323" class="el-menu-vertical-demo"
                text-color="#fff" router>
                <el-menu-item index="/article/category">
                    <el-icon>
                        <management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <user-filled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <user />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <edit-pen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>

        <el-container>
            <!-- 顶部区域 -->
            <el-header>
                <div>当前用户：<strong>admin</strong></div>
                <!-- 下拉菜单 -->
                <el-dropdown placement="bottom-end">
                    <span class="el-dropdown__box">
                        <el-avatar :src="avatar" />
                        <el-icon>
                            <caret-bottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="profile" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="password" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px; border: 1px solid red;">
                    content
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>big-event @2023</el-footer>
        </el-container>
    </el-container>
</template>


<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.jpg') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }

        .el-header {
            background-color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>
```



从这里开始涉及到了多个不同结构的页面，需要引入路由技术管理多个页面。



#### 客户端路由

- 路由：决定从起点到终点的路径的进程
- 在前端工程中，路由指的是根据不同的访问路径，展示不同组件的内容
- Vue Router 是 Vue.js 的官方路由

##### Vue Router

- 安装 vue-router

  ```bash
  pnpm add vue-router@4
  ```

- 在 src/router/index.js 中创建路由器，并导出

  ```js
  import { createRouter, createWebHistory } from 'vue-router'
  // 导入组件
  import Login from '@/views/Login.vue'
  import Layout from '@/views/Layout.vue'
  
  // 定义路由关系
  const routes = [
      { path: '/login', component: Login },
      { path: '/', component: Layout },
  ]
  
  // 创建路由器
  const router = createRouter({
      history: createWebHistory(),
      routes: routes
  })
  
  // 导出路由
  export default router
  ```

- 在 vue 应用实例中使用 vue-router

  如果使用 index.js 命名可以在导入的时候不提供文件名，会默认读取指定目录下的 index.js

  ```js
  import { createApp } from 'vue'
  import ElementPlus from 'element-plus'
  import 'element-plus/dist/index.css'
  import router from '@/router'
  
  import App from './App.vue'
  
  const app = createApp(App)
  app.use(router)
  app.use(ElementPlus)
  app.mount('#app')
  ```

- 声明 router-view 标签，展示组件内容
  在 App.vue 中使用 router-view，后续的组件就不需要直接导入到 App.vue，都通过 router-view 进行渲染。

  ```vue
  <script setup>
  </script>
  
  <template>
      <router-view></router-view>
  </template>
  
  <style scoped>
  </style>
  ```

  这时访问`/`路径可以跳转到 Layout 组件，访问`/login`路径可以跳转到 Login 组件。不过目前只是从 url 路径上设置了路由，在组件之间还没有实现路由跳转逻辑。

  由于页面跳转的逻辑和登录的行为密切相关，因此可以在登录函数的成功回调中实现页面跳转逻辑。借助 router 的`push`函数即可完成跳转

  ```vue
  <script lang="ts" setup>
  // ....
  
  // 登录数据模型，复用注册数据模型
  // 表单校验规则，复用注册表单校验规则
  // 调用登录接口
  import { useRouter } from 'vue-router'
  const router = useRouter()
  const login = async () => {
      let result = await userLoginService(registerData)
      ElMessage.success(result.message?result.message:'登录成功')
      // 路由跳转到首页
      router.push('/')
  }
  </script>
  ```

  

##### 子路由

根据前面的首页结构分析，还需要准备 5 个页面对应侧边导航栏的五个模块：

- ArticleCategory.vue
- ArticleManage.vue
- UserInfo.vue
- UserAvatar.vue
- UserResetPassword.vue

这 5 个页面都会展示到 Layout.vue 的右侧中间部分，属于二级路由，也是一级路由的子路由。

- Layout.vue
- Login.vue

这两个页面都会挂载到 App.vue 下，属于一级路由。

步骤：

- 编写好五个组件

  > 一开始为了方便验证路由是否正常工作，只需要提供一些提示信息即可，不需要完整设计出整个组件。

- 配置子路由

  在 index.js 中配置

  ```js
  // ....
  import ArticleCategory from '@/views/article/ArticleCategory.vue'
  import ArticleManage from '@/views/article/ArticleManage.vue'
  import UserInfo from '@/views/user/UserInfo.vue'
  import UserAvatar from '@/views/user/UserAvatar.vue'
  import UserResetPassword from '@/views/user/UserResetPassword.vue'
  
  // 定义路由关系
  const routes = [
      { path: '/login', component: Login },
      {
          path: '/', component: Layout, children: [
              { path: '/article/category', component: ArticleCategory },
              { path: '/article/manage', component: ArticleManage },
              { path: '/user/info', component: UserInfo },
              { path: '/user/avatar', component: UserAvatar },
              { path: '/user/resetPassword', component: UserResetPassword }
          ]
      },
  ]
  // ....
  ```

- 声明 router-view 标签

  在前面 Layout.vue 中用于展示主要内容的部分处编写 router-view，然后利用 element-plus 组件提供的属性，配置菜单项的路由路径

  ```vue
  <template>
      <!-- element-plus 容器 -->
      <el-container class="layout-container">
          <!-- 左侧菜单 -->
          <!-- .... -->
  
          <el-container>
              <!-- 顶部区域 -->
              <!-- .... -->
  
              <!-- 中间区域 -->
              <el-main>
                  <!-- <div style="width: 1290px; height: 570px; border: 1px solid red;">
                      content
                  </div> -->
                  <router-view></router-view>
              </el-main>
              <!-- 底部区域 -->
              <el-footer>big-event @2023</el-footer>
          </el-container>
      </el-container>
  </template>
  ```

- 为菜单项 el-menu-item 设置 index 属性，指定点击后的路由路径

  ```vue
  <template>
      <!-- element-plus 容器 -->
      <el-container class="layout-container">
          <!-- 左侧菜单 -->
          <el-aside width="200px">
              <div class="el-aside__logo"></div>
              <!-- element-plus 菜单 -->
              <el-menu active-text-color="#ffd04b" background-color="#232323" class="el-menu-vertical-demo"
                  text-color="#fff" router>
                  <el-menu-item index="/article/category">
                      <el-icon>
                          <management />
                      </el-icon>
                      <span>文章分类</span>
                  </el-menu-item>
                  <el-menu-item index="/article/manage">
                      <el-icon>
                          <promotion />
                      </el-icon>
                      <span>文章管理</span>
                  </el-menu-item>
                  <el-sub-menu>
                      <template #title>
                          <el-icon>
                              <user-filled />
                          </el-icon>
                          <span>个人中心</span>
                      </template>
                      <el-menu-item index="/user/info">
                          <el-icon>
                              <user />
                          </el-icon>
                          <span>基本资料</span>
                      </el-menu-item>
                      <el-menu-item index="/user/avatar">
                          <el-icon>
                              <crop />
                          </el-icon>
                          <span>更换头像</span>
                      </el-menu-item>
                      <el-menu-item index="/user/resetPassword">
                          <el-icon>
                              <edit-pen />
                          </el-icon>
                          <span>重置密码</span>
                      </el-menu-item>
                  </el-sub-menu>
              </el-menu>
          </el-aside>
  
          <el-container>
              <!-- 顶部区域 -->
              <!-- .... -->
              
              <!-- 中间区域 -->
              <!-- .... -->
              
              <!-- 底部区域 -->
              <el-footer>big-event @2023</el-footer>
          </el-container>
      </el-container>
  </template>
  ```

- 默认重定向页面

  访问`/`路径时自动重定向到一个组件，避免首次进入主页默认不展示任何内容，在路由中配置`redirect`项即可

  ```js
  // 定义路由关系
  const routes = [
      { path: '/login', component: Login },
      {
          path: '/', component: Layout, redirect: '/article/manage', children: [
              { path: '/article/category', component: ArticleCategory },
              { path: '/article/manage', component: ArticleManage },
              { path: '/user/info', component: UserInfo },
              { path: '/user/avatar', component: UserAvatar },
              { path: '/user/resetPassword', component: UserResetPassword }
          ]
      },
  ]
  ```

  访问`/`时会自动重定向到`/article/manage`。

  

#### 文章分类列表

在 api 目录下创建新的 article.js，编写请求函数

```js
import request from '@/utils/request.js'

// 文章分类列表查询
export const articleCategoryListService = () => {
    return request.get('/category');
}
```

在 ArticleCategory.vue 中调用

```vue
<script setup>
import { ref } from 'vue'
const categorys = ref([])
// 声明异步函数调用文章列表查询函数
import { articleCategoryListService } from '@/api/article.js'
const articleCategoryList = async() => {
    let result = await articleCategoryListService()
    console.log(result.data)
    categorys = result.data
}
articleCategoryList();
</script>
```

这时候访问并不能成功获取到数据，因为缺少了 JWT 认证的操作，导致直接访问接口只会得到 401 未授权的状态码。

需要在携带有效 token 的情况下，才能成功访问到对应的资源。前端携带 token 涉及到了状态管理，在 Vue 中提供了 Pinia 状态管理库。



#### 状态管理

Pinia 状态管理库：Pinia 是 Vue 专属状态管理库，它允许你跨组件或页面共享状态。

> 在 Login.vue 中获取的 token 无法直接共享给 ArticleCategory.vue，通过 Pinia 就可以将 Login.vue 中获取的 token 先存储到 Pinia 中管理，然后 ArticleCategory.vue 就可以从 Pinia 中获取该 token。

- 安装 Pinia

  ```bash
  pnpm add pinia
  ```

- 在 Vue 应用实例中使用 Pinia

  ```js
  import { createApp } from 'vue'
  import ElementPlus from 'element-plus'
  import 'element-plus/dist/index.css'
  import router from '@/router'
  import { createPinia } from 'pinia'
  
  import App from './App.vue'
  
  const app = createApp(App)
  const pinia = createPinia()
  app.use(pinia)
  app.use(router)
  app.use(ElementPlus)
  app.mount('#app')
  ```

- 在 src/stores/token.js 中定义 store

  ```js
  // 定义 store
  import { defineStore } from 'pinia'
  import { ref } from 'vue'
  
  /*
      第一个参数：store 的名字，唯一标识
      第二个参数：一个函数，返回一个对象，对象中包含了我们需要共享的数据和函数
  
      返回值：一个对象，包含了我们需要共享的数据和函数
  */
  export const useTokenStore = defineStore('token', () => {
      // 定义状态的内容
  
      // 响应式变量
      const token = ref('')
  
      // 定义函数，用于修改状态
      const setToken = (newToken) => {
          token.value = newToken
      }
  
      // 定义函数，用于移除状态
      const removeToken = () => {
          token.value = ''
      }
  
      return {
          token, setToken, removeToken
      }
  })
  ```

- 在组件中使用 store

  Login.vue

  ```vue
  <script lang="ts" setup>
  import { userRegisterService, userLoginService } from '@/api/user'
  
  // 登录数据模型，复用注册数据模型
  // 表单校验规则，复用注册表单校验规则
  // 调用登录接口
  import { useTokenStore } from '@/stores/token.js'
  import { useRouter } from 'vue-router'
  const router = useRouter()
  const tokenStore = useTokenStore()
  const login = async () => {
      let result = await userLoginService(registerData)
      ElMessage.success(result.message?result.message:'登录成功')
      // 把 token 保存到 pinia 中
      tokenStore.setToken(result.data)
      // 路由跳转到首页
      router.push('/')
  }
  </script>
  ```

- 在 article.js 的请求函数中设置 token 的请求头

  ```js
  import request from '@/utils/request.js'
  import { useTokenStore } from '@/stores/token.js'
  // 文章分类列表查询
  export const articleCategoryListService = () => {
      const tokenStore = useTokenStore()
      // 在 pinia 中定义的响应式数据不需要通过 .value 获取值
      return request.get('/category', { headers: { Authorization: tokenStore.token } });
  }
  ```



#### Axios 请求拦截器优化

在前面使用 Pinia 管理 token 的过程中，需要为每个请求函数设置请求头，比较繁琐。这可以通过配置请求拦截器统一处理。

在 request.js 中添加请求拦截器

```js
// 定制请求实例

// 导入 axios 
import axios from "axios";

import { ElMessage } from 'element-plus'
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

import { useTokenStore } from '@/stores/token.js'
// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 请求前的回调
        // 添加 token
        const tokenStore = useTokenStore()
        // 判断有没有 token
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        return config
    },
    (err) => {
        // 请求错误的回调
        return Promise.reject(err);
    }
)

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 判断业务状态码
        if (result.data.code === 0) {
            // 业务成功
            return result.data;
        }

        // 业务失败
        // alert(result.data.message?result.data.message:'请求失败，请稍后重试');
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        // 将异步状态改为失败状态，方便被 catch 捕获
        return Promise.reject(result.data);
        
    },
    err => {
        // alert('请求失败，请稍后重试');
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;
```

由请求拦截器统一处理 token 之后， article.js 可以不需要再手动设置 token

```js
import request from '@/utils/request.js'
// 文章分类列表查询
export const articleCategoryListService = () => {
    return request.get('/category')
}
```



#### token 丢失问题

这里在测试 token 认证功能时，如果刷新浏览器， token 就会丢失，需要重新登录，很不方便。这是因为 Pinia 默认为内存存储，可以借助 Pinia 的持久化插件 Persist 解决此问题。



#### Pinia 持久化插件 Persist

- Pinia 默认是内存存储，当刷新浏览器的时候会丢失数据。
- Persist 插件可以将 Pinia 中的数据持久化存储。

使用步骤：

- 安装 persist

  ```bash
  pnpm add pinia-plugin-persistedstate
  ```

- 在 pinia 中使用 persist

  修改 main.js，引入持久化插件并使用

  ```js
  import { createApp } from 'vue'
  import ElementPlus from 'element-plus'
  import 'element-plus/dist/index.css'
  import '@/assets/css/main.scss'
  import router from '@/router'
  import { createPinia } from 'pinia'
  import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
  
  import App from './App.vue'
  
  const app = createApp(App)
  const pinia = createPinia()
  pinia.use(piniaPluginPersistedstate)
  app.use(pinia)
  app.use(router)
  app.use(ElementPlus)
  app.mount('#app')
  ```

- 定义状态 Store 时指定持久化配置参数

  修改 token.js，配置持久化

  ```js
  // 定义 store
  import { defineStore } from 'pinia'
  import { ref } from 'vue'
  
  /*
      第一个参数：store 的名字，唯一标识
      第二个参数：一个函数，返回一个对象，对象中包含了我们需要共享的数据和函数
  
      返回值：一个对象，包含了我们需要共享的数据和函数
  */
  export const useTokenStore = defineStore('token', () => {
      // 定义状态的内容
  
      // 响应式变量
      const token = ref('')
  
      // 定义函数，用于修改状态
      const setToken = (newToken) => {
          token.value = newToken
      }
  
      // 定义函数，用于移除状态
      const removeToken = () => {
          token.value = ''
      }
  
      return {
          token, setToken, removeToken
      }
  },{
      persist: true // 持久化存储
  })
  ```

  

#### 未登录统一处理

前面的另一个问题是在未登录情况下能够直接访问文章分类页面(虽然不会渲染数据，但是可以访问这个页面，且没有提示信息)，实际上未登录用户在直接访问此页面时应该会先提示用户登录。

这个时候就需要对未登录状态进行统一的处理，可以在响应拦截器中设置对 401 响应状态码的处理并使用路由跳转到登录页面

request.js

```js
// 由于模块加载机制，这里不能直接使用 useRouter
// import { useRouter } from 'vue-router'
// const router =  useRouter()
// 可以直接导入 router 实例
import router from '@/router'

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 判断业务状态码
        if (result.data.code === 0) {
            // 业务成功
            return result.data;
        }

        // 业务失败
        // alert(result.data.message?result.data.message:'请求失败，请稍后重试');
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        // 将异步状态改为失败状态，方便被 catch 捕获
        return Promise.reject(result.data);
        
    },
    err => {
        // 判断响应状态码，如果为 401 则证明未登录，提示请登录并跳转到登录页面
        if (err.response.status === 401) {
            // 未登录
            ElMessage.error('请先登录')
            router.push('/login')
        } else {
            ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        }
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)
```

需要注意，这里的 router 不能通过`useRouter`函数获取，由于模块加载顺序问题，`useRouter`无法在 Vue 组件外部使用。在 Vue 组件以外，可以直接引入 router 导出的实例，以便在任何地方使用。



##### 关于`useRouter`函数和直接导入 `router` 实例

`useRouter` 是 Vue 3 Composition API 的一部分，它允许你在使用 Composition API 的组件内部获得对路由实例的访问。这是一种更加模块化和响应式的方式，特别适用于在 Vue 3 组件内部使用。

> 这种方式使得组件不需要直接依赖外部的 `router` 实例导入，而是通过 Vue 提供的钩子函数动态获取，这有助于保持组件的封装性和可测试性。

直接导入 `router` 实例是一种在任何地方访问路由功能的快捷方式，不仅限于 Vue 组件内部，例如在 `axios` 拦截器中。

> 这种方式依赖于你已经创建并导出了 `router` 实例。

区别和使用场景

- **`useRouter`** 主要用于 Vue 3 Composition API 的组件内部，提供了一种响应式且封装性好的方式来访问路由实例。
- **直接导入 `router` 实例** 可以在任何需要路由功能的地方使用，包括但不限于 Vue 组件，它提供了一种全局访问路由的方法。



#### 文章分类添加

添加弹窗组件以及响应的数据模型、校验规则到 ArticleCategory.vue

弹窗组件部分，注意在“添加分类”的按钮上绑定弹窗事件

```vue
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index" />
            <el-table-column label="分类名称" prop="categoryName" />
            <el-table-column label="分类别名" prop="categoryAlias" />
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" />
                    <el-button :icon="Delete" circle plain type="danger" />
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="暂无数据" />
            </template>
        </el-table>
        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" title="添加文章分类" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10" />
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>
```

数据模型和校验规则部分

```vue
<script setup>
// ....

// 控制添加分类弹窗可见性
const dialogVisible = ref(false)

// 添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
// 添加分类表单校验规则
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
</script>
```

为弹窗组件“确认”按钮绑定事件，调用添加文章分类的接口

```vue
<template>
    <el-card class="page-container">
        <!-- .... -->
        
        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" title="添加文章分类" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10" />
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCategory">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>
```

article.js 编写响应的请求函数

```js
// 添加文章分类
export const articleCategoryAddService = (categoryData) => {
    return request.post('/category', categoryData)
}
```

组件中的函数调用

```vue
<script setup>
// ....
// 声明异步函数调用文章列表查询函数
import { articleCategoryListService, articleCategoryAddService } from '@/api/article.js'
const articleCategoryList = async() => {
    let result = await articleCategoryListService()
    console.log(result.data)
    categorys.value = result.data
}
articleCategoryList();
// 控制添加分类弹窗可见性
const dialogVisible = ref(false)

// 添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
// 添加分类表单校验规则
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
import { ElMessage } from 'element-plus'; 
// 调用添加分类接口
const addCategory = async () => {
    // 调用接口
    let result = await articleCategoryAddService(categoryModel.value)
    ElMessage.success(result.message?result.message:'添加成功')

    // 调用获取所有文章分类的函数
    articleCategoryList()
    // 关闭弹窗
    dialogVisible.value = false
}
</script>
```



#### 文章分类修改

修改分类的弹窗和添加分类的弹窗结构基本一致，可以复用组件，只需要稍微弹窗标题和表单项即可。

修改如下

- 定义变量 `title` 用于控制弹窗标题，弹窗组件上绑定 `title` 变量
- 在添加分类和编写分类的按钮上绑定控制弹窗可见性的 `dialogVisible` 变量，同时设置 `title` 变量的值

```vue
<script setup>
// ....
// 控制添加分类弹窗可见性
const dialogVisible = ref(false)
// ....
// 定义变量，控制弹窗的标题
const title = ref('')
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true;title='添加文章分类'">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index" />
            <el-table-column label="分类名称" prop="categoryName" />
            <el-table-column label="分类别名" prop="categoryAlias" />
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="dialogVisible = true;title='编辑文章分类'" />
                    <el-button :icon="Delete" circle plain type="danger" />
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="暂无数据" />
            </template>
        </el-table>
        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10" />
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCategory">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>
```

接下来使数据回显到编辑分类的弹窗中。由于函数变得复杂，于是把编辑按钮上绑定的事件封装到单独的函数中调用。

```vue
<script setup>
// 展示编辑弹窗
const showDialog = (row) => {
    dialogVisible.value = true
    title.value = '编辑文章分类'
    // 数据拷贝
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    // 扩展 id 属性，用于后续更新操作
    categoryModel.value.id = row.id
}
</script>
<template>
<!-- .... -->
<el-table-column label="操作" width="100">
    <template #default="{ row }">
        <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)" />
        <el-button :icon="Delete" circle plain type="danger" />
</template>
</el-table-column>
<!-- .... -->
</template>
```



组件复用之后的一个问题是根据不同的情况调用不同的函数，在前面的弹窗组件中固定绑定的是 `addCategory` 函数，这可以根据组件中绑定的其它响应式变量进行动态调用。

```vue
<script setup>
    import { articleCategoryListService, articleCategoryAddService, articleCategoryUpdateService } from '@/api/article.js'
    // 编辑分类
    const updateCategory = async (row) => {
        // 调用接口
        let result = await articleCategoryUpdateService(categoryModel.value)

        ElMessage.success(result.message?result.message:'修改成功')

        // 调用获取所有分类的函数
        articleCategoryList()

        // 关闭弹窗
        dialogVisible.value = false
    }
</script>
<template>
<!-- .... -->
<el-button type="primary" @click="title==='添加文章分类'? addCategory() : updateCategory()">确认</el-button>
<!-- .... -->
</template>
```

article.js

```js
// 修改文章分类
export const articleCategoryUpdateService = (categoryData) => {
    return request.put('/category', categoryData)
}
```

之后就可以根据 `title` 的值判断是调用 `addCategory` 函数还是调用 `updateCategory` 函数。

这里在测试的时候还有一个问题，修改完分类后再点击添加分类按钮后，修改用的数据依旧保留在弹窗组件中，这是典型的复用模型但是没有清空数据的问题 (前面也提到过)，可以编写一个用于清空模型数据的函数。

```js
// 清空模型的数据
const clearData = () => {
    categoryModel.value.categoryName = ''
    categoryModel.value.categoryAlias = ''
}
```

然后可以直接绑定到对应的按钮上

```vue
<el-button type="primary" @click="dialogVisible = true;title='添加文章分类';clearData()">添加分类</el-button>
```



#### 文章分类删除

删除按钮绑定事件

```vue
<el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)" />
```

事件绑定的函数

```vue
<script setup>
    // 删除分类
    const deleteCategory = (row) => {
        // 弹出确认框
        ElMessageBox.confirm(
            '确认删除吗？',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
            .then(async () => {
            // 调用接口
            let result = await articleCategoryDeleteService(row.id)
            ElMessage({
                type: 'success',
                message: '删除成功'
            })
            // 刷新列表
            articleCategoryList()
        })
            .catch(() => {
            ElMessage({
                type: 'info',
                message: '操作取消'
            })
        })
    }
</script>
```

请求函数

```js
// 删除文章分类
export const articleCategoryDeleteService = (id) => {
    const data = { id: id }
    return request.delete('/category', { data })
}
```



#### 文章列表查询

##### 页面搭建

ArticleManage.vue 初步搭建

```vue
<script setup>
import { Delete, Edit } from '@element-plus/icons-vue';
import { ref } from 'vue';

// 文章分类数据模型
const categorys = ref([])

// 搜索时使用的 categoryId
const categoryId = ref('')

// 搜索时使用的 state
const state = ref('')

// 文章列表数据模型
const articles = ref([])

// 分页条数据模型
const pageNum = ref(1) // 当前页码
const total = ref(13) // 总条数
const pageSize = ref(5) // 每页显示条数

// 分页条大小改变时触发
const onSizeChange = (size) => {
    pageSize.value = size
}
// 分页条页码改变时触发
const onCurrentChange = (pageNum) => {
    pageNum.value = pageNum
}

// 回显文章分类列表
import { articleCategoryListService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}
articleCategoryList()
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索菜单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 200px">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id" />
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state" style="width: 200px">
                    <el-option label="已发布" value="已发布" />
                    <el-option label="草稿" value="草稿" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary">搜索</el-button>
                <el-button>重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title" />
            <el-table-column label="分类" prop="categoryId" />
            <el-table-column label="发布时间" prop="createTime" />
            <el-table-column label="状态" prop="state" />
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" />
                    <el-button :icon="Delete" circle plain type="danger" />
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3,5,10,15]"
        layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
        @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>
```

##### 接口调用

article.js 编写请求函数

```js
// 文章列表查询
export const articleListService = (params) => {
    return request.get('/article', { params: params })
}
```

ArticleManage.vue 调用函数

```vue
<script setup>
// 回显文章分类列表
import { articleCategoryListService, articleListService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}
articleCategoryList()

// 获取文章列表
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params)

    // 渲染视图
    total.value = result.data.total
    articles.value = result.data.items
}
articleList()
</script>
```

这里返回的 `categoryId` 是数字，直接展示在页面的分类项下面不直观。通过文章分类查询和文章列表查询的配合，可以为文章列表扩展一个分类名称的属性。

```vue
<script setup>
// 回显文章分类列表
import { articleCategoryListService, articleListService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}
articleCategoryList()

// 获取文章列表
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params)

    // 渲染视图
    total.value = result.data.total
    articles.value = result.data.items

    // 扩展：处理分类名称
    for (let i=0;i<articles.value.length;i++) {
        let article = articles.value[i]
        for(let j=0;j<categorys.value.length;j++) {
            if (article.categoryId === categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}
articleList()
</script>
```

##### 事件处理

为搜索和重置按钮绑定事件

```vue
<el-button type="primary" @click="articleList">搜索</el-button>
<el-button @click="categoryId='';state=''">重置</el-button>
```

##### 分页变化

为分页条绑定相关的响应式变量和函数

```vue
<script setup>
// 分页条数据模型
const pageNum = ref(1) // 当前页码
const total = ref() // 总条数
const pageSize = ref(5) // 每页显示条数

// 分页条大小改变时触发
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
// 分页条页码改变时触发
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}
</script>
```



#### 文章添加

##### 基本页面搭建

在 ArticleManage.vue 中加入抽屉组件

```vue
<!-- 抽屉 -->
<el-drawer v-model="drawerVisible" title="添加文章" direction="rtl" size="50%">
    <!-- 添加文章表单 -->
    <el-form :model="articleModel" label-width="100px">
        <el-form-item label="文章标题">
            <el-input v-model="articleModel.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="文章分类">
            <el-select v-model="articleModel.categoryId" placeholder="请选择">
                <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id" />
            </el-select>
        </el-form-item>
        <el-form-item label="文章封面">
            <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false">
                <img v-if="articleModel.cover" :src="articleModel.cover" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                    <plus />
                </el-icon>
            </el-upload>
        </el-form-item>
        <el-form-item label="文章内容">
            <div class="editor">富文本编辑器</div>
        </el-form-item>
        <el-form-item>
            <el-button type="primary">发布</el-button>
            <el-button type="info">草稿</el-button>
        </el-form-item>
    </el-form>
</el-drawer>
```

相关联的数据模型

```vue
<script setup>
import { Plus } from '@element-plus/icons-vue'
// 控制抽屉的显示与隐藏
const drawerVisible = ref(false)
// 添加表单数据模型
const articleModel = ref({
    title: '',
    content: '',
    cover: '',
    state: '',
    categoryId: ''
})
</script>
```

样式

```vue
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}
.editor {
    width: 100%;
    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
```



对于文章内容部分需要引入富文本编辑器，这里使用 VueQuill 开源富文本编辑器

官网地址：https://vueup.github.io/vue-quill/

安装

```bash
pnpm add @vueup/vue-quill@latest
```

导入组件和样式

```vue
<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
</script>
```

在页面中使用组件

```vue
<template>
  <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html" />
</template>
```

##### 接口调用

文件上传，可以直接利用 element-plus 上传组件的功能。

```vue
<el-form-item label="文章封面">
    <!-- 
auto-upload: 设置是否自动上传
action: 设置服务器接口路径
name: 设置上传的文件字段名
on-success: 设置上传成功的回调函数
-->
    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
               action="/api/upload" name="file" :headers="{'Authorization': tokenStore.token }"
               :on-success="uploadSuccess">
        <img v-if="articleModel.cover" :src="articleModel.cover" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon">
            <plus />
        </el-icon>
    </el-upload>
</el-form-item>
```

由于这里是借助组件功能发起请求，不受 axios 拦截器的影响，这里需要手动设置 token

```vue
<script setup>
// 导入 token
import { useTokenStore } from '@/stores/token.js';
const tokenStore = useTokenStore()

// 上传成功的回调函数
const uploadSuccess = (result) => {
    articleModel.value.cover = result.data
    console.log(result.data)
}
</script>
```



文章添加接口，参考接口文档，可以直接复用数据模型

按钮绑定事件

```vue
<el-button type="primary" @click="drawerVisible = true;clearData()">添加文章</el-button>
<!-- .... -->
<el-button type="primary" @click="addArticle('已发布')">发布</el-button>
<el-button type="info" @click="addArticle('草稿')">草稿</el-button>
```

函数调用实现

```vue
<script setup>
// 添加文章
const addArticle = async (state) => {
    // 把发布状态赋值给数据模型
    articleModel.value.state = state
    // 调用接口
    let result = await articleAddService(articleModel.value)
    ElMessage.success(result.message?result.message:'添加成功')
    // 关闭抽屉
    drawerVisible.value = false
    // 刷新当前列表
    articleList()
}
// 清空模型的数据
const clearData = () => {
    articleModel.value = {
        title: '',
        content: '',
        cover: '',
        state: '',
        categoryId: ''
    }
}
</script>
```

article.js 请求函数实现

```js
// 文章添加
export const articleAddService = (articleData) => {
    return request.post('/article', articleData)
}
```



#### 文章修改

可参考此 [commit](https://github.com/s-chance/spring-boot-3-adventures/commit/898de71d89b6ec9848e2f399ded22b097e7d455c) 查看文件变更内容

#### 文章删除

可参考此 [commit](https://github.com/s-chance/spring-boot-3-adventures/commit/15c50428b2ebd148b23043ec6ce61fd4f1b06bf1) 查看文件变更内容



#### 顶部导航栏信息

- 定义 Store

  在 stores 目录下新建  userInfo.js

  ```js
  import { defineStore } from "pinia"
  import { ref } from 'vue'
  const useUserInfoStore = defineStore('userInfo', () => {
      // 定义状态相关内容
  
      const info = ref({})
  
      const setInfo = (newInfo) => {
          info.value = newInfo
      }
  
      const removeInfo = () => {
          info.value = {}
      }
  
      return {
          info, setInfo, removeInfo
      }
  },
  {
      persist: true
  })
  
  export default useUserInfoStore
  ```

- 接口函数

  在 api 目录下的 user.js 中编写接口函数

  ```js
  // 获取用户信息
  export const userInfoService = () => {
      return request.get('/user/userInfo')
  }
  ```

- 接口调用

  在 Layout.vue 中调用

  ```vue
  <script setup>
  import { userInfoService } from '@/api/user.js'
  import useUserInfoStore from '@/stores/userInfo.js'
  const userInfoStore = useUserInfoStore()
  // 调用函数，获取用户详细信息
  const getUserInfo = async () => {
      // 调用接口
      let result = await userInfoService()
      // 数据存储到 Pinia 中
      userInfoStore.setInfo(result.data)
  }
  
  getUserInfo()
  </script>
  <template>
  <!-- .... -->
  <div>当前用户：<strong>{{ userInfoStore.info.nickname }}</strong></div>
  <!-- .... -->
  <el-avatar :src="userInfoStore.info.avatar ? userInfoStore.info.avatar : avatar" />
  </template>
  ```



#### 下拉菜单功能

##### 退出登录

使用 element-plus 下拉菜单组件提供的功能

```vue
<!-- 下拉菜单 -->
<!-- command: 条目被点击后会被触发，在事件函数上可以声明一个参数，接收条目对应的指令 -->
<el-dropdown placement="bottom-end" @command="handleCommand">
    <span class="el-dropdown__box">
        <el-avatar :src="userInfoStore.info.avatar ? userInfoStore.info.avatar : avatar" />
        <el-icon>
            <caret-bottom />
        </el-icon>
    </span>
    <template #dropdown>
<el-dropdown-menu>
    <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
    <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
    <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
    <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
        </el-dropdown-menu>
    </template>
</el-dropdown>
```

退出登录函数实现

```vue
<script setup>
import { userInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
const router = useRouter()
// 条目被点击后，调用的函数
const handleCommand = (command) => {
    // 判断指令
    if (command === 'logout') {
        // 退出登录
        // 弹出确认框
    ElMessageBox.confirm(
        '确认要退出登录吗？',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    )
    .then(async () => {
        // 退出登录
        // 1.清除 Pinia 中的 token 和 userInfo
        tokenStore.removeToken()
        userInfoStore.removeInfo()
        // 2.跳转回登录页
        router.push('/login')
        ElMessage({
            type: 'success',
            message: '退出登录成功'
        })

    })
    .catch(() => {
        ElMessage({
            type: 'info',
            message: '操作取消'
        })
    })
    } else {
        // 路由
        router.push('/user/' + command)
    }
}
</script>
```



#### 基本资料修改

##### 基本页面

UserInfo.vue 基本页面搭建

```vue
<script setup>
import { ref } from 'vue'
const userInfo = ref({
    id: 2,
    username: 'black',
    nickname: 'ack',
    email: 'black@ack.com'
})
const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled />
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname" />
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="userInfo.email" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style scoped>
</style>
```



##### 数据回显

使用 Pinia 获取数据赋值即可

```vue
<script setup>
import useUserInfoStore from '@/stores/userInfo';
const userInfoStore = useUserInfoStore()
const userInfo = ref({...userInfoStore.info})
</script>
```



##### 事件绑定

提交按钮上绑定事件和函数

```vue
<el-button type="primary" @click="updateUserInfo">提交修改</el-button>
```

在 user.js 中编写请求函数

```js
// 修改个人信息
export const userInfoUpdateService = (userInfoData) => {
    return request.put('/user/update', userInfoData)
}
```

在 UserInfo.vue 中调用函数

```vue
<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo';
const userInfoStore = useUserInfoStore()
const userInfo = ref({...userInfoStore.info})
// ....
import { userInfoUpdateService } from '@/api/user.js';
import { ElMessage } from 'element-plus';
// 修改个人信息
const updateUserInfo = async () => {
    // 调用接口
    let result = await userInfoUpdateService(userInfo.value)
    ElMessage.success(result.message?result.message:'修改成功')

    // 修改 pinia 中的个人信息
    userInfoStore.setInfo(userInfo.value)
}
</script>
```



#### 用户头像修改

##### 基本页面

UserAvatar.vue 基本页面搭建

```vue
<script setup>
import { Plus, Upload } from '@element-plus/icons-vue';
import { ref } from 'vue'
import avatar from '@/assets/avatar.webp'
const uploadRef = ref()

const avatarUrl = avatar
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false">
                    <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
                    <img v-else :src="avatar" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large" @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large">
                    上传图片
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>
```

##### 数据回显

从 pinia 获取用户信息实现头像图片回显

```vue
<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/avatar.webp'
const uploadRef = ref()

import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore()

// 用户头像地址
const avatarUrl = ref(userInfoStore.info.avatar)
</script>
```

##### 图片上传

使用 element-plus 上传组件提供的功能，配置上传组件的属性

```vue
<el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false"
           :auto-upload="true" action="/api/upload" name="file" :headers="{ 'Authorization': tokenStore.token}"
           :on-success="uploadSuccess">
    <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
    <img v-else :src="avatar" width="278" />
</el-upload>
```

相应的回调函数

```vue
<script setup>
mport { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()

// 图片上传成功的回调函数
const  uploadSuccess = (result) => {
    avatarUrl.value = result.data
}
</script>
```

给上传按钮绑定事件和函数

```vue
<el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
    上传图片
</el-button>
```

user.js 编写请求函数

```js
// 修改头像
export const userAvatarUpdateService = (avatarUrl) => {
    const params = new URLSearchParams()
    params.append('avatarUrl', avatarUrl)
    return request.patch('/user/updateAvatar', params)
}
```

在 UserAvatar.vue 组件中调用函数

```vue
<script setup>
import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore()
// ....
import { userAvatarUpdateService } from '@/api/user.js'; 
import { ElMessage } from 'element-plus';
// 头像修改
const updateAvatar = async () => {
    // 调用接口
    let result = await userAvatarUpdateService(avatarUrl.value)

    ElMessage.success(result.message?result.message:'头像修改成功')

    // 同步 pinia 中的用户信息
    userInfoStore.info.avatar = avatarUrl.value
}
</script>
```



#### 重置密码

可参考此 [commit](https://github.com/s-chance/spring-boot-3-adventures/commit/804a0523a6b4996924efda2a796ad8f5b6de9b29) 查看文件变更内容
