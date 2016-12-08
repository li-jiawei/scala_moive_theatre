$.fn.formSubmit = function(options,_13f){
    var _21,_22 = 'post',_23 = 'application/x-www-form-urlencoded';
    var ifr = '<iframe id="_13ifr" name="_13ifr" style="display:none"></iframe>';
    $("body").append(ifr);
    var _11 = $('#_13ifr')[0];
    _11.onload = _11.onreadystatechange = function () {
        if(!this.readyState || this.readyState == 'complete'){
            var _14 = _13ifr.document.body.innerHTML;
            _14 = _14.replace(/<\/?.+?>/g,"");
            if(_14 != null && _14 !='' && !_14.startWith('404')){
                _13f(_14);
            }
            document.body.removeChild($("#_13ifr")[0]);
        }
    };
    if(typeof options == "string"){
        _21 = options;
    }else{
        _21 = options.url;
        if(options.method != null && options.url != ''){
            _22 = options.method;
        }
        if(options.enctype != null && options.enctype != ''){
            _23 = options.enctype;
        }
    }
    this.attr('method',_22);
    this.attr('target','_13ifr');
    this.attr('action',_21);
    this.attr("enctype",_23);
    this.submit();
    this.removeAttr('target');
};