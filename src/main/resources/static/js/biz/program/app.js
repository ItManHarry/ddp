var app = new Vue({
	el:"#app",
	components:{
		SystemMenu
	},
	data:function(){	
		return{
			title:'项目管理系统',
			total:10,							//记录总条数
			items:5,							//每页条数
			page: 1,							//当前页
			height:window.screen.height * 0.55,	//表格最大高度
			formTitle:'新增项目',				//项目窗体标题
			formVisible:false,					//项目窗体显示	
			groupVisible:false,					//项目成员窗体
			statusVisible:false,				//项目状态窗体
			invoiceVisible:false,				//项目发票信息窗体
			formLabelWidth1:'120px',
			formLabelWidth2:'140px',
			ibuttonText:'新增',
			form:{
				id:'',
				code:'',
				name:'',
				remark:'',
				startdate:'',
				enddate:'',
				amount:'',
				contractno:'',
				prno:'',
				svnadd:''
			},
			stsform:{
				id:'',
				company:'',
				newpro:'1',
				category:'1',
				state:'',
				possible:'',
				contractstart:'',
				contractend:'',
				legalorg:'',
				legaldept:'',
				ddicdept:'',
				budget:'',
				process:''
			},
			invcform:{
				id:'',
				stage:'1',
				percent:'',
				invoicedt:''
			},
			rules:{
				name:[
					{required:true, message:'请输入项目名称!', trigger:'blur'}
				],
				contractno:[
					{required:true, message:'请输入合同编号!', trigger:'blur'}
				],
				prno:[
					{required:true, message:'请输入PR编号!', trigger:'blur'}
				],
				remark:[
					{required:true, message:'请输入项目描述!', trigger:'blur'}
				],
				startdate: [
	            	{ required: true, message: '请选择项目开始日期', trigger: 'blur' }
	          	],
	          	enddate: [
	            	{ required: true, message: '请选择项目结束日期', trigger: 'blur' }
	          	],
	          	amount: [
	            	{ required: true, message: '请输入项目金额', trigger: 'blur' }
	          	]
			},			
			stsrules:{
				company:[
					{required:true, message:'请输入客户公司!', trigger:'blur'}
				],
				possible:[
					{required:true, message:'请输入执行可行性!', trigger:'blur'}//,
					//{type: 'number', message: '可行性必须是0 ~ 100的整数!'}
				],
				contractstart: [
	            	{ required: true, message: '请选择合同开始日期', trigger: 'blur' }
	          	],
	          	contractend: [
	            	{ required: true, message: '请选择合同结束日期', trigger: 'blur' }
	          	],
	          	legalorg:[
					{required:true, message:'请选择法人!', trigger:'blur'}
				],
				legaldept:[
					{required:true, message:'请输入客户公司主管部门!', trigger:'blur'}
				],						
				ddicdept: [
	            	{ required: true, message: '请选择我司主管部门', trigger: 'blur' }
	          	],
	          	budget:[
					{required:true, message:'请输入事业预算!', trigger:'blur'}//,
					//{type: 'number', message: '事业预算必须是数字!'}
				],
				process:[
					{required:true, message:'请输入进行情况!', trigger:'blur'}
				]
			},
			invcrules:{
				stage:[
					{required:true, message:'请选择发票区分!', trigger:'blur'}
				],
				percent:[
					{required:true, message:'请输入支付比例!', trigger:'blur'}
				],
				invoicedt:[
					{required:true, message:'请填写开票时间!', trigger:'blur'}
				]
			},		
			yn:[],
			cs:[],
			states:[],							//项目状态
			proData: [],						//项目数据list
			invcData:[],						//项目发票数据
			stages:[],							//项目发票区分
			searchkey:'',						//搜索值
			searchtype:true,					//是否按照姓名查找,默认为true,false为按照code查找
			users: [],							//系统用户
	        members: [],						//项目组人员,
	        proId:''							//项目UUID	
		}
	},
	methods:{
		//重置form
		resetFormValues:function(){
			this.form.id = ''
			this.form.code = ''
			this.form.name = ''
			this.form.remark = ''
			this.form.startdate = ''
			this.form.enddate = ''
			this.form.amount = ''
			this.form.contractno = ''
			this.form.prno = ''
			this.form.svnadd = ''
		},
		//关闭项目维护窗体
		closePro:function(){					
			this.formVisible = false
			this.resetFormValues()
		},
		//新增项目
		addPro:function(f){					
      		this.formTitle = '新增项目'
			this.formVisible = true
			this.$refs['proForm'].resetFields()
			this.resetFormValues()
		},
		//编辑项目信息
      	editPro:function(index, row){
      		this.formVisible = true
      		this.formTitle = '修改项目'
      		this.form.id = row.tid
      		this.form.code = row.code
      		this.form.name = row.name
      		this.form.remark = row.remark
      		this.form.startdate = row.startdate
      		this.form.enddate = row.enddate
      		this.form.amount = row.amount
      		this.form.contractno = row.contractno
      		this.form.prno = row.prno
      		this.form.svnadd = row.svnadd
      		this.$refs['proForm'].clearValidate()
      	},
      	//维护项目成员
      	groupUsers:function(index, row){
      		this.proId = row.tid
      		this.groupVisible = true
      		//获取成员数据
      		axios.get('/pm/biz/pro/group/members', {
      		    params: {
      		      proId:row.tid		      		      
      		    }
      		}).then(function (response) {
      			var result = response.data		      			
      			app.users = result.data.users
      			app.members = result.data.members
      		    console.log(response)
      		}).catch(function (error) {
      			alert("error")
      		    console.log(error)
      		})
      	},
      	//保存项目组成员信息
      	saveGroup:function(){
      		//alert(this.proId)
      		//alert(this.members.join(","))
      		axios.post('/pm/biz/pro/group/save',	{
      			proId:app.proId,
      			members:app.members.join(","),
            	user:'admin'
            }).then(function (response) {
            	var result = response.data
            	if(result.status == 0)
            		app.$message({
            			message:result.message,
            			type:'warning',
            			duration:2000,
            			offset:120
            		})	
            }).catch(function (error) {
            	app.$message({
        			message:result.message,
        			type:'danger',
        			duration:2000,
        			offset:120
        		})	
            });
      	},
		//执行保存项目
		savePro:function(form) {
	        this.$refs[form].validate(function(valid) {
	        	if (valid) {
	        		axios.post('/pm/biz/pro/save',	{
		            	id:app.form.id,
		            	code:app.form.code,
		            	name:app.form.name,
		            	remark:app.form.remark,
		            	startdate:app.form.startdate,
		            	enddate:app.form.enddate,
		            	amount:app.form.amount,
		            	contractno:app.form.contractno,
		            	prno:app.form.prno,
		            	status:1,
		            	svnadd:app.form.svnadd,
		            	user:'admin'
		            }).then(function (response) {
		            	var result = response.data
		            	if(result.status == 0)
		            		app.$message({
		            			message:result.message,
		            			type:'warning',
		            			duration:2000,
		            			offset:120
		            		})	
		            	else{		            		
		            		app.formVisible = false
		            		app.getProData()		            		
		            	}			            		
		            }).catch(function (error) {
		            	app.$message({
	            			message:result.message,
	            			type:'danger',
	            			duration:2000,
	            			offset:120
	            		})	
		            });
	        	}else{
	            	console.log('error submit!!')
	            	return false;
	            }
	        });
      	},
      	//获取项目状态信息
      	proStatus:function(index, row){
      		this.proId = row.tid
      		this.statusVisible = true
      		//获取项目状态数据
      		axios.get('/pm/biz/pro/status/info', {
      		    params: {
      		      proId:row.tid		      		      
      		    }
      		}).then(function (response) {
      			app.$refs['proStatusForm'].clearValidate()
      			var result = response.data	
      			if(result.data){
      				//alert('Set status data')
      				app.stsform.id = result.data.tid
	            	app.stsform.company = result.data.company
	            	app.stsform.newpro = result.data.newpro+""
	            	app.stsform.category = result.data.category+""
	            	app.stsform.state = result.data.state+""
	            	app.stsform.possible = result.data.possible
	            	app.stsform.contractstart = result.data.contractstart
	            	app.stsform.contractend = result.data.contractend
	            	app.stsform.legalorg = result.data.legalorg
	            	app.stsform.legaldept = result.data.legaldept
	            	app.stsform.ddicdept = result.data.ddicdept
	            	app.stsform.budget = result.data.budget
	            	app.stsform.process = result.data.process
      			}
      		    console.log(response)
      		}).catch(function (error) {
      			alert("error")
      		    console.log(error)
      		})
      	},
      	//获取项目发票信息
      	getInvoiceData:function(){
      		//获取项目发票数据
      		axios.get('/pm/biz/pro/invoice/list', {
      		    params: {
      		      proId:app.proId  		      
      		    }
      		}).then(function (response) {
      			var result = response.data	
      			if(result.data){
      				//alert('Set status data')
      				app.invcData = result.data
      			}
      		    console.log(response)
      		}).catch(function (error) {
      			alert("error")
      		    console.log(error)
      		})	
      	},
      	//维护项目发票信息
      	proInvoice:function(index, row){
      		this.proId = row.tid
      		//获取项目发票数据
      		this.getInvoiceData()
      		this.invoiceVisible = true
      		//清空表单
      		this.resetInvoice()
      	},
      	editInvoice:function(row, column, event){
      		this.invcform.id = row.tid
      		this.invcform.stage = row.stage
      		this.invcform.percent = row.percent
      		this.invcform.invoicedt = row.invoicedt
      		this.ibuttonText = '修改'
      		this.$refs['invcform'].clearValidate()
      	},
      	//保存项目发票信息
      	saveInvoice:function(form){
      		this.$refs[form].validate(function(valid) {
	        	if (valid) {
	        		axios.post('/pm/biz/pro/invoice/save',	{
	        			proId:app.proId,
		            	id:app.invcform.id,
		            	stage:app.invcform.stage,
		            	percent:app.invcform.percent,
		            	invoicedt:app.invcform.invoicedt,
		            	user:'admin'
		            }).then(function (response) {
		            	var result = response.data
		            	if(result.status == 0)
		            		app.$message({
		            			message:result.message,
		            			type:'warning',
		            			duration:2000,
		            			offset:120
		            		})	
		            	else{		            		
		            		app.resetInvoice()
		            		app.getInvoiceData()	  
		            	}			            		
		            }).catch(function (error) {
		            	app.$message({
	            			message:result.message,
	            			type:'danger',
	            			duration:2000,
	            			offset:120
	            		})	
		            });
	        	}else{
	            	console.log('error submit!!')
	            	return false;
	            }
	        });
      	},
      	//重置项目发票表单
      	resetInvoice:function(){
      		this.invcform.id = ''
      		this.invcform.stage = ''
      		this.invcform.percent = ''
      		this.invcform.invoicedt = ''
      		this.ibuttonText = '新增'
      		this.$refs['invcform'].clearValidate()
      	},
      	//获取项目状态数据
  		getProStates:function(){
      		axios.get('/pm/sys/enum/options', {
      		    params: {
      		      code:'D003'
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.states = result.data
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
  		},
  		//获取是否下拉列表
  		getYns:function(){
      		axios.get('/pm/sys/enum/options', {
      		    params: {
      		      code:'D004'
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.yn = result.data
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
  		},
  		//获取是否下拉列表
  		getStages:function(){
      		axios.get('/pm/sys/enum/options', {
      		    params: {
      		      code:'D006'
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.stages = result.data
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
  		},
  		//获取项目分类数据
  		getCs:function(){
      		axios.get('/pm/sys/enum/options', {
      		    params: {
      		      code:'D005'
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.cs = result.data
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
  		},
      	//保存项目状态信息
      	saveProStatus:function(form){
      		this.$refs[form].validate(function(valid) {
	        	if (valid) {
	        		axios.post('/pm/biz/pro/status/save',	{
	        			proId:app.proId,
		            	id:app.stsform.id,
		            	company:app.stsform.company,
		            	newpro:app.stsform.newpro,
		            	category:app.stsform.category,
		            	state:app.stsform.state,
		            	possible:app.stsform.possible,
		            	contractstart:app.stsform.contractstart,
		            	contractend:app.stsform.contractend,
		            	legalorg:app.stsform.legalorg,
		            	legaldept:app.stsform.legaldept,
		            	ddicdept:app.stsform.ddicdept,
		            	budget:app.stsform.budget,
		            	process:app.stsform.process,
		            	user:'admin'
		            }).then(function (response) {
		            	var result = response.data
		            	if(result.status == 0)
		            		app.$message({
		            			message:result.message,
		            			type:'warning',
		            			duration:2000,
		            			offset:120
		            		})	
		            	else{		            		
		            		app.statusVisible = false
		            	}			            		
		            }).catch(function (error) {
		            	app.$message({
	            			message:result.message,
	            			type:'danger',
	            			duration:2000,
	            			offset:120
	            		})	
		            });
	        	}else{
	            	console.log('error submit!!')
	            	return false;
	            }
	        });
      	},
      	//分页
      	changePage:function(page){
      		this.page = page
      		this.getProData()
      	},
      	//获取项目清单数据
      	getProData:function(){
      		axios.get('/pm/biz/pro/pm/query', {
      		    params: {
      		      page:app.page,
      		      limit:app.items,
      		      name:app.searchtype ? app.searchkey : '',
      		      code:app.searchtype ? '' : app.searchkey
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.proData = result.data
      			app.total = result.number
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
      	}
	},
	mounted:function(){
		//alert('Mounted!!!')
		this.getProData()
		this.getYns()
		this.getCs()
		this.getProStates()
		this.getStages()
	}
});	