# stone-compiler

根据《两周自制脚本语言》，由 Java 实现的 Stone 语言编译器

### 基本语法


```
// 变量声明与输出
hello = "hello world!"
print(hello)
```

函数
```
// 函数使用def定义
def fib(n) {
    if n < 2 {
        // 无需显式return 返回值是最后一条执行语句的结果
        n
    } else {
        fib(n-1) + fib(n-2)
    }
}
// 55
print(fib(10))
```
闭包
```
def add(a) {
    // 闭包使用fun定义
    fun (b) {
        a+b
    }
}
three = add(3)
// 8
print(three(5))
// 10
print(three(7))
```
类与继承
```
// class 定义类
class Position {
    x = y = 0
    def move(nx, ny) {
        x = nx; y = ny
    }
}
// extends 继承
class Pos3D extends Position {
    z = 0
    def move(nx, ny, nz) {
        x = nx; y = ny; z = nz
    }
}
// 用 new 来实例化对象
p = Pos3D.new
p.move(3, 4, 5)
// 12
print(p.x + p.y + p.z)
```
数组
```
a = [2, 3, 4]
// 3
print(a[1])
a[1] = "three"
// a[1]: three
print("a[1]: " + a[1])
b = [["one\", 1], ["two", 2]]
// two: 2
print(b[1][0] + ": " + b[1][1])
```