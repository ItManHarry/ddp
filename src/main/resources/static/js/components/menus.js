var SystemMenu = {
	template:`
		<div>
			<el-tooltip style="margin:4px;" effect="light" placement="bottom-start">
				<div slot="content">
					<div v-for = "(menu,index) in menus">
						<hr>
						<el-link :underline="false" :href = "menu.url" :key = "index"><i class = "fa fa-angle-double-right"></i>&nbsp;&nbsp;{{menu.menuname}}</el-link>
					</div>
				</div>
				<el-button icon="el-icon-menu"></el-button>
			</el-tooltip>
		</div>
	`,
	data:function(){
		return {
			menus:[]
		}
	},
	props:[
		
	],
	mounted:function(){		
		var app = this
		//获取菜单权限
		axios.get('/pm/sys/auth/menus', {
  		    params: {
  		      
  		    }
  		}).then(function (response) {
  			var result = response.data
  			//alert(result.data)  			
  			app.menus = result.data
  		    console.log(response)
  		}).catch(function (error) {
  		    console.log(error)
  		})
	}
}