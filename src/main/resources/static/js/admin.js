layui.define(['jquery', 'form', 'layer', 'element'], function (exports) {
    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer,
        element = layui.element;
    var menu = [];
    var curMenu;
    form.verify({
        psw2: function (value, item) {
            var newPsw1 = $("input[name='newPsw']").val();
            if (value != newPsw1) {
                return '两次输入密码不一致';
            }
        },
        pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
    });



    $(function () {
        tableCheck = {
            init: function () {
                $(".layui-form-checkbox").click(function (event) {
                    if ($(this).hasClass('layui-form-checked')) {
                        $(this).removeClass('layui-form-checked');
                        if ($(this).hasClass('header')) {
                            $(".layui-form-checkbox").removeClass('layui-form-checked');
                        }
                    } else {
                        $(this).addClass('layui-form-checked');
                        if ($(this).hasClass('header')) {
                            $(".layui-form-checkbox").addClass('layui-form-checked');
                        }
                    }
                });
            },
            getData: function () {
                var obj = $(".layui-form-checked").not('.header');
                var arr = [];
                obj.each(function (index, el) {
                    arr.push(obj.eq(index).attr('data-id'));
                });
                return arr;
            }
        };

        tableCheck.init();
        //延时加载
        setTimeout(function () {
            if (sessionStorage.getItem("menu")) {
                menu = JSON.parse(sessionStorage.getItem("menu"));
                for (var i = 0; i < menu.length; i++) {
                    tab.tabAdd(menu[i].title, menu[i].url, menu[i].id);
                }
            } else {
                return false;
            }
            if (sessionStorage.getItem("curMenu")) {
                $('.layui-tab-title').find('layui-this').removeClass('layui-class');
                curMenu = JSON.parse(sessionStorage.getItem("curMenu"));
                id = curMenu.id;
                if (id) { //因为默认桌面首页不存在lay-id,所以要对此判断
                    $('.layui-tab-title li[lay-id="' + id + '"]').addClass('layui-this');
                    tab.tabChange(id);
                } else {
                    $(".layui-tab-title li").eq(0).addClass('layui-this'); //未生�?
                    $('.layui-tab-content iframe').eq(0).parent().addClass('layui-show');
                }
            } else {
                $(".layui-tab-title li").eq(0).addClass('layui-this'); //未生�?
                $('.layui-tab-content iframe').eq(0).parent().addClass('layui-show');

            }
        }, 100);

    });


    $('.container .left_open i').click(function (event) {
        if ($('.left-nav').css('left') == '0px') {
            //此处左侧菜单是显示状态，点击隐藏
            $('.left-nav').animate({
                left: '-221px'
            }, 100);
            $('.page-content').animate({
                left: '0px'
            }, 100);
            $('.page-content-bg').hide();
        } else {
            //此处左侧菜单是隐藏状态，点击显示
            $('.left-nav').animate({
                left: '0px'
            }, 100);
            $('.page-content').animate({
                left: '221px'
            }, 100);
            //点击显示后，判断屏幕宽度较小时显示遮罩
            if ($(window).width() < 768) {
                $('.page-content-bg').show();
            }
        }
    });

    //点击遮罩背景，左侧菜单隐
    $('.page-content-bg').click(function (event) {
        $('.left-nav').animate({
            left: '-221px'
        }, 100);
        $('.page-content').animate({
            left: '0px'
        }, 100);
        $(this).hide();
    });

    /**
     * 左侧菜单事件
     * 如果有子级就展开，没有就打开frame
     */
    $('.left-nav #nav li').click(function (event) {
        if ($(this).children('.sub-menu').length) {
            if ($(this).hasClass('open')) {
                $(this).removeClass('open');
                $(this).find('.nav_right').html('&#xe697;');
                $(this).children('.sub-menu').stop().slideUp();
                $(this).siblings().children('.sub-menu').slideUp();
            } else {
                $(this).addClass('open');
                $(this).children('a').find('.nav_right').html('&#xe6a6;');
                $(this).children('.sub-menu').stop().slideDown();
                $(this).siblings().children('.sub-menu').stop().slideUp();
                $(this).siblings().find('.nav_right').html('&#xe697;');
                $(this).siblings().removeClass('open');
            }
        } else {
            var url = $(this).children('a').attr('target_url');
            console.log(url)

            var title = $(this).find('cite').html();
            var index = $('.left-nav #nav li').index($(this));

            for (var i = 0; i < $('.weIframe').length; i++) {
                if ($('.weIframe').eq(i).attr('tab-id') == index + 1) {
                    tab.tabChange(index + 1);
                    event.stopPropagation();
                    return;
                }
            }
            ;

            tab.tabAdd(title, url, index + 1);
            tab.tabChange(index + 1);
        }
        event.stopPropagation();
    });


    var tab = {
        tabAdd: function (title, url, id) {
            //判断当前id的元素是否存在于tab�?
            var li = $("#WeTabTip li[lay-id=" + id + "]").length;
            if (li > 0) {
                //tab已经存在，直接切换到指定Tab
                element.tabChange('wenav_tab', id); //切换到：用户管理
            } else {
                element.tabAdd('wenav_tab', {
                    title: title,
                    content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="weIframe"></iframe>',
                    id: id
                });
                //当前窗口内容

            }
            FrameWH(); //计算框架高度

        },
        tabDelete: function (id) {
            element.tabDelete("wenav_tab", id); //删除
            //removeStorageMenu(id);

        },
        tabChange: function (id) {
            //切换到指定Tab�?
            element.tabChange('wenav_tab', id);
        },
        tabDeleteAll: function (ids) { //删除所�?
            $.each(ids, function (i, item) {
                element.tabDelete("wenav_tab", item);
            })
        }
    };

    $("#rightMenu li").click(function () {
        var type = $(this).attr("data-type");
        var layId = $(this).attr("data-id")
        if (type == "current") {
            //console.log("close this:" + layId);
            tab.tabDelete(layId);
        } else if (type == "all") {
            //console.log("closeAll");
            var tabtitle = $(".layui-tab-title li");
            var ids = new Array();
            $.each(tabtitle, function (i) {
                ids[i] = $(this).attr("lay-id");
            })
            tab.tabDeleteAll(ids);
        } else if (type == "fresh") {
            //console.log("fresh:" + layId);
            tab.tabChange($(this).attr("data-id"));
            var othis = $('.layui-tab-title').find('>li[lay-id="' + layId + '"]'),
                index = othis.parent().children('li').index(othis),
                parents = othis.parents('.layui-tab').eq(0),
                item = parents.children('.layui-tab-content').children('.layui-tab-item'),
                src = item.eq(index).find('iframe').attr("src");
            item.eq(index).find('iframe').attr("src", src);
        } else if (type == "other") {
            var thisId = layId;
            $('.layui-tab-title').find('li').each(function (i, o) {
                var layId = $(o).attr('lay-id');
                if (layId != thisId && layId != 0) {
                    tab.tabDelete(layId);
                }
            });

            $('.rightMenu').hide();
        }
    });

    /*
     * @todo 重新计算iframe高度
     */
    function FrameWH() {
        var h = $(window).height() - 164;
        $("iframe").css("height", h + "px");
    }

    $(window).resize(function () {
        FrameWH();
    });

    /**
     *
     * @param title 标题
     * @param url 请求的url
     * @param w 弹出层宽度（缺省调默认值）
     * @param h 弹出层高度（缺省调默认值）
     * @constructor
     */
    window.WeAdminShow = function (title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        if (url == null || url == '') {
            url = "404.html";
        }
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        layer.open({
            type: 2,


            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: true,
            shade: 0.4,
            title: title,
            content: url
        });
    };


    /**
     * 删除单个产线或单个联系人
     * @param modelName 模块名，contact代表联系人，prodline代表产线
     * @param id 要删除的产线或者联系人的id
     * @returns {boolean} 是否删除成功
     */
    window.memDel = function (modelName, id) {
        var msg = "";
        var url = "";
        if (modelName === "prodline") {
            msg = "您确定要删除该产线系统配置？";
            url = "/prodline/del/" + id;
        } else if (modelName === "contact") {
            msg = "您确定要删除该联系人配置？";
            url = "/contact/del/" + id;
        } else {
            return false;
        }
        layer.confirm(msg, {
            title: '删除提示',
            btn: ['确定', '取消'] //按钮
        }, function () {
            //layer.msg("您要删除的是" + id)
            $.ajax({
                url: url,
                type: 'delete',
                dataType: 'json',
                success: function (result) {
                    layer.alert(result.msg);
                    window.location.reload();
                },
                error: function () {

                }
            })
        });
    };

    /**
     * 删除所有，暂未提供
     */
    window.delAll = function () {
        layer.alert("该功能暂未开放！");
    };


    window.updatePwd = function () {
        var index = layer.open({
            type: 1,
           // skin: 'layui-layer-rim', //加上边框
            //area: ['420px', '240px'], //宽高
            content: $("#pwdModel")
        });
        // 取消按钮，关闭弹出层
        $("#pswCancel").on('click', function () {
            layer.close(index);
        });
        form.on("submit(pswSubmit)", function () {
            $.ajax({
               url: '/update/pwd',
               type: 'put',
               dataType: 'json',
               data: {
                   "oldPwd": $("input[name='oldPsw']").val(),
                   "newPwd": $("input[name='newPsw']").val()
               },
               success: function (result) {
                   if (result.code == 0) {
                       layer.alert("修改密码成功，下次登录生效。");
                       layer.close(index);
                   }else {
                       layer.msg(result.msg, {anim:6});
                   }
               } 
            });
            return false;
        });
    };

    $('.layui-tab-close').on('click', function () {
        $('.layui-tab-title li').eq(0).find('i').remove();
    });


    exports('admin', {});
});