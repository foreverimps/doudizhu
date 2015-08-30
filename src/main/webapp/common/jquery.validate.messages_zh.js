/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
jQuery.extend(jQuery.validator.messages, {
        required: "必选字段",
		remote: "请修正该字段",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
		minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
		rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
		min: jQuery.validator.format("请输入一个最小为 {0} 的值"),
		decimal:"只能保留两位小数"
});

function checkIdcard(num){  
    var len = num.length, re;  
    if (len == 15)  
        re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);  
    else if (len == 18)  
        re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\d)$/);  
    else{  
        return false;  
    }  
    var a = num.match(re);  
    if (a != null){  
        if (len==15){  
            var D = new Date("19"+a[3]+"/"+a[4]+"/"+a[5]);  
            var B = D.getYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5];  
        }else{  
            var D = new Date(a[3]+"/"+a[4]+"/"+a[5]);  
            var B = D.getFullYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5];  
        }  
        if (!B){  
            return false;  
        }  
    }  
    return true;  
} 


// 字符验证
jQuery.validator.addMethod("string", function(value, element) {
 return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "不允许包含特殊符号!");

// 手机号码验证
jQuery.validator.addMethod("mobile", function(value, element) {
 var length = value.length;
 return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/.test(value));
}, "手机号码格式错误!");

// 电话号码验证
jQuery.validator.addMethod("phone", function(value, element) {
 var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
 return this.optional(element) || (tel.test(value));
}, "电话号码格式错误!");

// 验证值小数位数不能超过两位
jQuery.validator.addMethod("decimal", function(value, element) {
 var decimal = /^-?\d+(\.\d{1,2})?$/;
 return this.optional(element) || (decimal.test(value));
}, $.validator.format("小数位数不能超过两位!"));

//字母和数字的验证
jQuery.validator.addMethod("charnum", function(value, element) {
    var chrnum = /^([a-zA-Z0-9]+)$/;
    return this.optional(element) || (chrnum.test(value));
}, "只能输入数字和字母(字符A-Z, a-z, 0-9)");

//下拉框验证
$.validator.addMethod("selectOne", function(value, element) {
    return value == "请选择";
}, "必须选择一项");

//添加验证方法 (身份证号码验证)  
jQuery.validator.addMethod("idcard", function(value, element) {     
    return this.optional(element) || checkIdcard(value);     
}, "请正确输入您的身份证号码");  

jQuery.extend(jQuery.validator.defaults, {
    errorElement: "span",
    success:"valid"
});