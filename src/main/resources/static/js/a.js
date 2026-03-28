// 自定义指令：监听input事件
app.directive('ngInput', ['$timeout', function($timeout) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ngModel) {
            var searchTimer = null;
            var lastValue = '';

            element.on('input', function() {
                var value = element.val() || '';

                // 直接检查内容长度，不管是否在输入法状态
                if(value.length >= 2) {
                    // 避免重复触发相同的搜索
                    if(value === lastValue) {
                        return;
                    }
                    lastValue = value;

                    // 取消之前的定时器
                    if(searchTimer) {
                        $timeout.cancel(searchTimer);
                    }

                    // 设置新的定时器
                    searchTimer = $timeout(function() {
                        scope.$apply(function() {
                            // 调用控制器中的方法
                            scope.onInputChange(value);
                        });
                    }, 300);
                } else {
                    // 内容少于2个字符，清空搜索结果
                    lastValue = '';
                    if(searchTimer) {
                        $timeout.cancel(searchTimer);
                    }
                    scope.$apply(function() {
                        scope.onInputChange('');
                    });
                }
            });

            // 清理
            scope.$on('$destroy', function() {
                element.off('input');
                if(searchTimer) {
                    $timeout.cancel(searchTimer);
                }
            });
        }
    };
}]);