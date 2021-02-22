var ProgramChart = {
	template:`
		<div>
			<div id="program" style="width:100%;height:200px;"></div>
		</div>
	`,
	data:function(){
		return {
			option: {
	            tooltip: {
	            	trigger:"axis"	//不设置默认为item
	            },
	            calculable:true,
	            xAxis: [{
	            	type:"category",
	                data: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		              axisLabel:{
		            	  formatter:'{value} 月'	//个性化设置坐标值
		              }
	            }],
	            yAxis: [{
	            	type:"value",
		              axisLabel:{
		            	  formatter:'{value} mm'	//个性化设置坐标值
		              }
	            }],
	            series: [{
	                name: '蒸发量',
	                type: 'bar',
	                data: [2,4.9,7,23.2,25.6,76.7,135.6,162.2,32.6,20,6.4,3.3]
	            },{
	                name: '降水量',
	                type: 'bar',
	                data: [2.6,5.6,9,26.4,28.7,70.7,175.6,182.2,48.7,18.8,6,2.3]
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
var IssueChart = {
	template:`
		<div>
			<div id="issue" style="width:100%;height:200px;"></div>
		</div>
	`,
	data:function(){
		return {
			option: {
				tooltip: {
	            	trigger:"item",	//不设置默认为item
	            	formatter:"{a} <br>{b} : {c} ({d}%)" //a:系列名称 b:数据项名称 c:数值 d:(饼图：百分比 | 雷达图：指标名称)
	            },
	            calculable:true,
	            series: [{
	            	name:"访问来源",
	            	type:"pie",
	            	radius:"70%", //半径：支持绝对值（px）和百分比， 百分比计算比：min(width,height) / 2 * 75%,传数组实现环形图[内半径，外半径]
	            	center:["50%","60%"], //圆心坐标：支持绝对值（px）和百分比， 百分比计算比：min(width,height) * 50%
	            	itemStyle:{
	            		normal:{
	            			label:{
	            				show:true,
	            				formatter:"{b}: {c} ({d}%)"
	            			}
	            		}
	            	},
	            	data:[{
            			value:335,
            			name:"直接访问"
            		},{
            			value:310,
            			name:"邮件营销"
            		},{
            			value:234,
            			name:"联盟广告"
            		},{
            			value:135,
            			name:"视频广告"
            		},{
            			value:1548,
            			name:"搜索引擎"
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
		this.draw()
	}
}