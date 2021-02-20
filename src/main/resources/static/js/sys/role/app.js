var app = new Vue({
	el:"#app",
	components:{
		SystemMenu
	},
	data:function(){				
		return{
			total:10,							//记录总条数
			items:10,							//每页条数
			page: 1,							//当前页
			height:window.screen.height * 0.55,	//表格最大高度
			formTitle:'新增角色',				//角色窗体标题
			formVisible:false,					//角色窗体显示
			menuVisible:false,					//菜单权限窗体显示	
			formLabelWidth:'120px',
			form:{
				id:'',
				rolename:'',
				status:1
			},
			rules:{
				rolename:[
					{required:true, message:'请输入角色名称!', trigger:'blur'}
				]
			},					
			roleData: [],						//角色数据list
			menuData: [],						//菜单数据list
			menuSelected: [],					//选择的菜单
			menuAuthed:[]						//已授权菜单
		}
	},
	methods:{
		//关闭角色维护窗体
		closeRole:function(){
			this.formVisible = false
			this.form.id = ''
			this.form.rolename = ''
		},
		//新增角色信息
		addRole:function(f){
      		this.formTitle = '新增角色'
			this.formVisible = true
			this.$refs[f].resetFields()
			this.form.id = ''
			this.form.rolename = ''
			this.form.status = 1
		},
		//编辑角色信息
      	editRole:function(index, row){
      		//alert(row.tid)
      		this.formVisible = true
      		this.formTitle = '修改角色'
  			this.form.id = row.tid
      		this.form.rolename = row.rolename
      		this.form.status = row.stsStr == '在用' ? 1 : 2
      		this.$refs['roleForm'].clearValidate()
      	},
      	//停用角色
      	unuseRole:function(index, row){
      		this.form.id = row.tid
      		this.form.rolename = row.rolename
      		this.form.status = 2
      		this.doSaveRole()
      	},
      	//启用角色
      	reuseRole:function(index, row){
      		this.form.id = row.tid
      		this.form.rolename = row.rolename
      		this.form.status = 1
      		this.doSaveRole()
      	},
		//执行保存角色信息
		saveRole:function(form) {
	        this.$refs[form].validate(function(valid) {
	          if (valid) {
	            app.doSaveRole()		            
	          } else {
	            console.log('error submit!!')
	            return false;
	          }
	        });
      	},
      	//执行保存
      	doSaveRole:function(){
      		axios.post('/pm/sys/role/save', {
            	id:app.form.id,
            	rolename:app.form.rolename,
            	status:app.form.status,
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
            		app.getOrgData()
            	}			            		
            }).catch(function (error) {
            	alert(error)
            });
      	},
      	//分配系统权限
      	roleMenu:function(index, row){
      		this.form.id = row.tid
      		this.menuVisible = true
      		axios.get('/pm/sys/menu/items', {
      		    params: {
      		      roleid:row.tid
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.menuData = result.data.menus
      			app.menuAuthed = result.data.authed
      			app.toggleSelection()
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
      	},
      	saveAuth:function(){
      		var ids = new Array()
      		if(this.menuSelected.length > 0){
	      		for(var i = 0; i < this.menuSelected.length; i++)
	      			ids.push(this.menuSelected[i].tid)
	      		//执行保存
	      		axios.post('/pm/sys/role/auth', {
	            	role:app.form.id,
	            	menus:ids.join(',')
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
	            		app.menuVisible = false
	            	}			            		
	            }).catch(function (error) {
	            	alert(error)
	            })
      		}else{
      			app.$message({
        			message:'请选择菜单!',
        			type:'warning',
        			duration:2000,
        			offset:120
        		})	
      		}
      	},
      	//设置选中
      	toggleSelection:function() {
      		this.$nextTick(()=>{
      		  this.menuData.forEach(row => {
      		    if(this.menuAuthed.indexOf(row.tid) >= 0){
      		      this.$refs.menuList.toggleRowSelection(row, true)
      		    }
      		  })
      		})
        },
        //点击行选中
        handleTrChange(row, event, column){
            this.$refs.menuList.toggleRowSelection(row)
        },
        //设置已选中的值
      	handleSelectionChange:function(val) {
      		this.menuSelected = val
        },
      	//分页
      	changePage:function(page){
      		this.page = page
      		this.getOrgData()
      	},
      	//获取角色清单数据
      	getOrgData:function(){
      		axios.get('/pm/sys/role/all', {
      		    params: {
      		      page:app.page,
      		      limit:app.items
      		    }
      		}).then(function (response) {
      			var result = response.data
      			app.roleData = result.data
      			app.total = result.number
      		    console.log(response)
      		}).catch(function (error) {
      		    console.log(error)
      		})
      	}
	},
	mounted:function(){
		//alert('Mounted!!!')
		this.getOrgData()
	}
});		