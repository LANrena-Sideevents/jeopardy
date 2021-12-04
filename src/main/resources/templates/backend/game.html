<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:object="${gamecontroller.game}">
<head>
    <script src="http://cdn.jsdelivr.net/webjars/jquery/3.6.0/jquery.min.js"
            th:src="@{/webjars/jquery/3.6.0/jquery.min.js}"></script>
    <script src="http://cdn.jsdelivr.net/webjars/tether/1.4.0/js/tether.min.js"
            th:src="@{/webjars/tether/1.4.0/js/tether.min.js}"></script>
    <script src="http://cdn.jsdelivr.net/webjars/bootstrap/5.1.1/js/bootstrap.min.js"
            th:src="@{/webjars/bootstrap/5.1.1/js/bootstrap.min.js}" defer="defer"></script>

    <link rel="stylesheet"
          src="http://cdn.jsdelivr.net/webjars/bootstrap/5.1.1/css/bootstrap.min.css"
          th:href="@{/webjars/bootstrap/5.1.1/css/bootstrap.min.css}"/>

    <link rel="stylesheet" href="/css/backend.css"/>
</head>
<body>

<div th:replace="backend/fragments/navigation :: navigation-full"></div>

<div class="container">
    <ol class="breadcrumb">
        <li><a th:href="@{|/backend|}">Jeopardy</a></li>
        <li class="active" th:text="*{id}"></li>
    </ol>

    <div class="panel panel-default">
        <table class="table equal-width">
            <thead>
            <tr>
                <th th:each="category : *{categories}">
                    <span th:text="${category.label}"/>
                </th>
            </tr>
            </thead>
            <tr th:each="i : ${#numbers.sequence(1, 5)}">
                <td th:each="j : ${#numbers.sequence(1, 5)}">
                    <a th:href="@{|/backend/game/*{id}/field/${j}/${i}|}"
                       th:text="${ i*100 }" th:unless="${gamecontroller.getFieldController(j, i).field.done}"/>
                    <span th:text="${ i*100 }" th:if="${gamecontroller.getFieldController(j, i).field.done}"
                          th:remove="tag"/>
                </td>
            </tr>
        </table>
    </div>
</div>

</body>
</html>
