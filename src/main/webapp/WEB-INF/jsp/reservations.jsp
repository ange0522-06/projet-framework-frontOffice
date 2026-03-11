<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.List, com.teamLead.reservationVoiture.dto.ReservationDto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Liste des réservations</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        
        h1 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5rem;
            font-weight: 300;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .filter-form {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            border-left: 4px solid #667eea;
        }
        
        .filter-form label {
            display: inline-block;
            margin-right: 15px;
            font-weight: 500;
            color: #495057;
        }
        
        .filter-form input[type="date"] {
            padding: 8px 12px;
            border: 2px solid #dee2e6;
            border-radius: 6px;
            margin-left: 8px;
            margin-right: 15px;
            transition: border-color 0.3s ease;
        }
        
        .filter-form input[type="date"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .filter-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 10px 25px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        
        .filter-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        
        .separator {
            color: #6c757d;
            font-weight: 500;
            margin: 0 10px;
        }
        
        table { 
            border-collapse: collapse; 
            width: 100%;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        
        th, td { 
            border: none;
            padding: 15px 20px; 
            text-align: left;
        }
        
        th { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-size: 0.9rem;
        }
        
        tbody tr {
            transition: background-color 0.3s ease;
        }
        
        tbody tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        
        tbody tr:hover {
            background-color: #e3f2fd;
            transform: scale(1.02);
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }
        
        td {
            color: #495057;
            font-weight: 400;
        }
        
        .no-data {
            text-align: center;
            color: #6c757d;
            font-style: italic;
            padding: 40px 20px;
        }
        
        @media (max-width: 768px) {
            .container {
                margin: 10px;
                padding: 20px;
            }
            
            h1 {
                font-size: 2rem;
            }
            
            .filter-form {
                padding: 15px;
            }
            
            .filter-form label {
                display: block;
                margin-bottom: 10px;
            }
            
            table {
                font-size: 0.9rem;
            }
            
            th, td {
                padding: 10px;
            }
        }
    </style>
</head>
<body>
<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<% Boolean showTable = (Boolean) request.getAttribute("showTable"); %>
<% String token = (String) request.getAttribute("token"); %>

<% if (errorMessage != null && !errorMessage.isEmpty() && (showTable == null || !showTable)) { %>
    <div style="color: red; font-weight: bold; margin-bottom: 16px;">
        <%= errorMessage %>
    </div>
<% } else { %>
    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div style="color: red; font-weight: bold; margin-bottom: 16px;">
            <%= errorMessage %>
        </div>
    <% } %>
    <% if (token != null && !token.isEmpty()) { %>
        <div style="color: green; font-weight: bold; margin-bottom: 16px;">
            Token : <%= token %>
        </div>
    <% } %>
    <h1>Liste des réservations</h1>
    <form method="get" action="${pageContext.request.contextPath}/front/reservations">
        <label>Date (exacte): <input type="date" name="date" /></label>
        &nbsp;ou&nbsp;
        <label>Du: <input type="date" name="from" /></label>
        <label>Au: <input type="date" name="to" /></label>
        <button type="submit">Filtrer</button>
    </form>
    <br/>
    <table>
        <thead>
        <tr>
            <th>Client</th>
            <th>Hôtel</th>
            <th>Date de réservation</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<ReservationDto> list = (List<ReservationDto>) request.getAttribute("reservations");
            if (list != null && !list.isEmpty()) {
                for (ReservationDto r : list) {
        %>
        <tr>
            <td><%= r.getCustomerName() %></td>
            <td><%= r.getHotelName() %></td>
            <td><%= r.getArrivalDate() %></td>
        </tr>
        <%      }
            } else { %>
            <tr>
                <td colspan="4">Aucune réservation trouvée.</td>
            </tr>
        <% } %>
        </tbody>
    </table>
<!-- <% } %>
    <div class="container">
        <h1>Liste des réservations</h1>

        <div class="filter-form">
            <form method="get" action="${pageContext.request.contextPath}/front/reservations">
                <label>Date (exacte): <input type="date" name="date" /></label>
                <span class="separator">ou</span>
                <label>Du: <input type="date" name="from" /></label>
                <label>Au: <input type="date" name="to" /></label>
                <button type="submit" class="filter-btn">Filtrer</button>
            </form>
        </div>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Client</th>
                <th>Hôtel</th>
                <th>Date d'arrivée</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<ReservationDto> list = (List<ReservationDto>) request.getAttribute("reservations");
                if (list != null && !list.isEmpty()) {
                    for (ReservationDto r : list) {
            %>
                <tr>
                    <td><%= r.getId() %></td>
                    <td><%= r.getCustomerName() %></td>
                    <td><%= r.getHotelName() %></td>
                    <td><%= r.getArrivalDate() %></td>
                </tr>
            <%      }
                } else { %>
                <tr>
                    <td colspan="4" class="no-data">Aucune réservation trouvée.</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>-->
</body>
</html> 
