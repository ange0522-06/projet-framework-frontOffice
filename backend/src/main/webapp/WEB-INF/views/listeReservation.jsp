<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des reservations</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/reservations.css' />" />
</head>
<body>
<div class="page">
    <h1>Liste des reservations</h1>

    <form class="filter" method="get" action="<c:url value='/front/reservations/filter' />">
        <div class="field">
            <label for="startDate">Date debut</label>
            <input type="date" id="startDate" name="startDate" value="${startDate}" />
        </div>
        <div class="field">
            <label for="endDate">Date fin</label>
            <input type="date" id="endDate" name="endDate" value="${endDate}" />
        </div>
        <button type="submit">Filtrer</button>
        <a class="reset" href="<c:url value='/front/reservations' />">Reinitialiser</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Client</th>
            <th>Nb personnes</th>
            <th>Date et heure</th>
            <th>Hotel</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty reservations}">
                <tr>
                    <td colspan="5" class="empty">Aucune reservation trouvee.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${reservations}" var="reservation">
                    <tr>
                        <td>${reservation.id}</td>
                        <td>${reservation.client}</td>
                        <td>${reservation.nbPeople}</td>
                        <td>${reservation.dateheure}</td>
                        <td>
                            <c:choose>
                                <c:when test="${reservation.hotel != null}">
                                    ${reservation.hotel.name}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
</body>
</html>
