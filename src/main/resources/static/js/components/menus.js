var SystemMenu = {
	template:`
		<div>
			<el-tooltip style="margin:4px;" effect="light" placement="bottom-start">
				<div slot="content">
					<div v-for = "(menu,index) in menus">
						<br>
						<el-link :underline="true" :href = "menu.url" :key = "index"><i class = "el-icon-arrow-right"></i>&nbsp;&nbsp;{{menu.menuname}}</el-link>
					</div>
				</div>
				<el-button icon="el-icon-menu"></el-button>
			</el-tooltip>
			&nbsp;&nbsp;<i class = "el-icon-location-information"></i>&nbsp;&nbsp;<span style = "font-size:16px;color:#888;">{{location}}</span>
		</div>
	`,
	data:function(){
		return {
			menus:[]
		}
	},
	props:[
		'location'
	],
	mounted:function(){		
		var app = this
		//获取菜单权限
		axios.get('/pm/sys/auth/menus', {
  		    params: {}
  		}).then(function (response) {
  			var result = response.data
  			app.menus = result.data
  		    console.log(response)
  		}).catch(function (error) {
  		    console.log(error)
  		})
	}
}