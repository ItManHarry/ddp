var Domain = window.location.protocol + "//" + window.location.host;
Vue.prototype.$Image = Domain + "/comm/file/get/"; //下载文件地址

var commonMixin = {
    data: function data() {
        return {

        };
    },
    methods:{
        queryExport: function queryExport(searchParam,exportUrl) {
            var that = this;
            return new Promise(function (resolve, reject) {
                //console.log(that.exportUrl + ' 请求数据，参数=>', this.queryData)
                axios.defaults.headers['Content-Type'] = 'application/json';
                axios({
                    method: 'get',
                    url: exportUrl,
                    // 请求地址
                    data: searchParam,
                    // 参数
                    responseType: 'blob' // 表明返回服务器返回的数据类型
                }).then(function (response) {
                    resolve(response.data);
                    var blob = new Blob([response.data], {
                        type: 'application/zip;charset=UTF-8'
                    });
                    var contentDisposition = response.headers['content-disposition'];
                    contentDisposition = decodeURI(contentDisposition);
                    var patt = new RegExp("filename=([^;]+\\.[^\\.;]+);*");
                    var result = patt.exec(contentDisposition);
                    var fileName = result[1];

                    if (window.navigator.msSaveOrOpenBlob) {
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        var link = document.createElement('a');
                        link.download = fileName;
                        link.style.display = 'none';
                        link.href = window.URL.createObjectURL(blob);
                        document.body.appendChild(link);
                        link.click(); //释放内存

                        window.URL.revokeObjectURL(link.href);
                        document.body.removeChild(link);
                    }

                    that.loading = false;
                }, function (err) {
                    reject(err);
                    that.loading = false;
                });
            });
        }
    }

}