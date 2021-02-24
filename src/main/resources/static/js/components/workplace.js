/*
 * 	Issue现况柱状图
 * 	取进行中的项目issue数量
 */
var IssueChart = {
	template:`
		<div>
			<div id="program" :style="style"></div>
		</div>
	`,
	data:function(){
		return {
			style:{
				width:'100%',
				height:window.screen.height * 0.5+'px'
			},
			option: {
	            tooltip: {
	            	trigger:"axis"	//不设置默认为item
	            },
	            calculable:true,
	            xAxis: [{
	            	type:"category",
	                data: ["微信三期升级","数据中台二期","培训系统升级","DSES系统升级","信审升级","财务报销系统"],
		              axisLabel:{
		            	  interval:0,
                          rotate:30
		              }
	            }],
	            yAxis: [{
	            	type:"value"
	            }],
	            series: [{
	                name: '待确认',
	                type: 'bar',
	                data: [5,2,6,3,6,3]
	            },{
	                name: '处理中',
	                type: 'bar',
	                data: [20,21,12,18,19,23]
	            },{
	                name: '已取消',
	                type: 'bar',
	                data: [4,7,2,5,8,4]
	            },{
	                name: 'Reopen',
	                type: 'bar',
	                data: [2,1,4,6,2,4]
	            },{
	                name: '处理完成',
	                type: 'bar',
	                data: [20,12,32,23,15,18]
	            }]
		    }
		}
	},
	props:[
		
	],
	methods:{
		draw:function() {
	      //通过echarts初始化我们的div
	      let issueChart = echarts.init(document.getElementById('program'))
	      //为这个echarts的图添加属性
	      issueChart.setOption(this.option, true)
	    }
	},
	mounted:function(){		
		this.draw()
	}
}
/**
 * 各个状态下的项目现况
 */
var ProgramChart = {
	template:`
		<div>
			<div id="issue" :style="style"></div>
		</div>
	`,
	data:function(){
		return {
			style:{
				width:'100%',
				height:window.screen.height * 0.5+'px'
			},
			option: {
				tooltip: {
	            	trigger:"item",							//不设置默认为item
	            	formatter:"{a} <br>{b} : {c} ({d}%)" 	//a:系列名称 b:数据项名称 c:数值 d:(饼图：百分比 | 雷达图：指标名称)
	            },
	            calculable:true,
	            series: [{
	            	name:"项目现况",
	            	type:"pie",
	            	radius:"70%", 			//半径：支持绝对值（px）和百分比， 百分比计算比：min(width,height) / 2 * 75%,传数组实现环形图[内半径，外半径]
	            	center:["50%","50%"], 	//圆心坐标：支持绝对值（px）和百分比， 百分比计算比：min(width,height) * 50%
	            	itemStyle:{
	            		normal:{
	            			label:{
	            				show:true,
	            				formatter:"{b}: {c}"
	            			}
	            		}
	            	},
	            	data:[{
            			value:2,
            			name:"等待"
            		},{
            			value:0,
            			name:"合同准备"
            		},{
            			value:3,
            			name:"起案进行"
            		},{
            			value:5,
            			name:"进行中"
            		},{
            			value:30,
            			name:"结束"
            		}]
	            }]
		    }
		}
	},
	props:[
		
	],
	methods:{
		draw:function() {
	      //通过echarts初始化我们的div
	      let issueChart = echarts.init(document.getElementById('issue'))
	      //为这个echarts的图添加属性
	      issueChart.setOption(this.option, true)
	    }
	},
	mounted:function(){		
		var app = this
		//获取菜单权限
		axios.get('/pm/biz/pro/charts', {
  		    params: {}
  		}).then(function (response) {
  			var result = response.data
  			app.option.series[0].data = result.data
  			app.draw()
  		    console.log(response)
  		}).catch(function (error) {
  		    console.log(error)
  		})
	}
}