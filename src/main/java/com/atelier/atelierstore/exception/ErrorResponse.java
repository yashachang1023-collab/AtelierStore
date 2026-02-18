package com.atelier.atelierstore.exception;

// 暂时删掉那三个 Lombok 注解，我们用纯 Java 代码
public class ErrorResponse {
    private int status;
    private String message;

    // 1. 手动写无参构造函数（JPA/Spring 序列化需要）
    public ErrorResponse() {
    }

    // 2. 手动写带参数的构造函数（你在 Handler 里用的就是这个）
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // 3. 手动写 Getter 和 Setter (可以用 Command + N 快速生成)
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}