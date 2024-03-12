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
   pnpm install element-plus
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





#### 客户端路由



#### 状态管理