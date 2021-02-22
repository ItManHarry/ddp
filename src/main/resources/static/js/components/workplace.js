var ProgramChart = {
	template:`
		<div>
			<div id="main" style="width:100%;height:800px;"></div>
		</div>
	`,
	data:function(){
		return {
			option: {
	            /*title: {
	                text: '烟台地区蒸发量和降水量',
	                //left:'center'
	                //backgroundColor:'#000',
	                //borderColor:'#ccc',
	                //borderWidth:2,
	                padding:5,
	                textStyle:{
	                	color:'#000'
	                },
	                subtext:'(2018-01-01 ~ 2018-08-31)',
	                subtextStyle:{
	                	color:'#888'
	                }
	            },*/
	            tooltip: {
	            	trigger:"axis"	//不设置默认为item
	            },
	            legend: {
	                data:['蒸发量','降水量'] //必须和series里的各个name属性值一只
	            },
	            /*toolbox:{
	            	show:true,
	            	feature:{
	            		mark:{
	            			show:true
	            		},
	            		dataView:{
	            			show:true,
	            			readOnly:true
	            		},
	            		magicType:{
	            			show:true,
	            			type:['line','bar']
	            		},
	            		restore:{
	            			show:true
	            		},
	            		saveAsImage:{
	            			//show:true
	            		}
	            	}
	            },*/
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
	      let proChart = echarts.init(document.getElementById('main'))
	      //为这个echarts的图添加属性
	      proChart.setOption(this.option, true)
	    }
	},
	mounted:function(){		
		this.draw()
	}
}