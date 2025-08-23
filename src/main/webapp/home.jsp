<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <!-- Funciones -->
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ShopLite • Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-body .d-flex {
            flex-direction: row;
            gap: 10px;
        }
        .card-body .btn-sm {
            width: auto;
        }
    </style>
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg bg-white border-bottom">
    <div class="container">
        <a class="navbar-brand text-primary" href="${pageContext.request.contextPath}/home">ShopLite</a>
        <div class="ms-auto d-flex gap-2">
            <c:if test="${sessionScope.role == 'ADMIN'}">
                <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/admin">Nuevo producto (Admin)</a>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/auth/logout">
                <button class="btn btn-sm btn-outline-danger">Cerrar sesión</button>
            </form>
        </div>
    </div>
</nav>

<section class="container my-5">
    <h2 class="mb-3">Productos</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            <c:out value="${error}"/>
        </div>
    </c:if>
    <c:if test="${param.msg != null}">
        <div class="alert alert-success" role="alert">
            <c:out value="${param.msg}"/>
        </div>
    </c:if>

    <div class="row g-3">
        <c:forEach var="p" items="${items}">
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <h5 class="card-title"><c:out value="${p.name}"/></h5>
                        <p class="text-muted">$<c:out value="${p.price}"/></p>
                        <p class="text-muted">Stock: <c:out value="${p.stock}"/></p>
                        <c:if test="${sessionScope.role == 'ADMIN'}">
                            <div class="d-flex gap-2">
                                <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editModal"
                                        onclick="setEditModal(${p.id}, '${fn:escapeXml(p.name)}', ${p.price}, ${p.stock})">Editar</button>
                                <form action="${pageContext.request.contextPath}/admin/delete-product" method="post"
                                      onsubmit="return confirm('¿Seguro que deseas eliminar ${fn:escapeXml(p.name)}?');">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">Eliminar</button>
                                </form>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty items}">
            <p class="text-muted">No hay productos disponibles.</p>
        </c:if>
    </div>

    <!-- Modal Editar -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">Editar Producto</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/admin/edit-product" method="post"
                      onsubmit="return validateEditForm()">
                    <div class="modal-body">
                        <input type="hidden" name="id" id="editId">
                        <div class="mb-3">
                            <label for="editName" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="editName" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="editPrice" class="form-label">Precio</label>
                            <input type="number" step="0.01" min="0.01" class="form-control" id="editPrice" name="price" required>
                        </div>
                        <div class="mb-3">
                            <label for="editStock" class="form-label">Stock</label>
                            <input type="number" min="0" class="form-control" id="editStock" name="stock" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Paginacion -->
    <c:if test="${total > 0}">
        <div class="d-flex justify-content-between align-items-center mt-4">
            <a class="btn btn-outline-secondary ${page == 1 ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/home?page=${page - 1}&size=${size}">« Anterior</a>
            <span class="text-muted">Página ${page} de ${totalPages}</span>
            <a class="btn btn-outline-secondary ${page >= totalPages ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/home?page=${page + 1}&size=${size}">Siguiente »</a>
        </div>
    </c:if>
</section>

<script>
    function setEditModal(id, name, price, stock) {
        document.getElementById('editId').value = id;
        document.getElementById('editName').value = name;
        document.getElementById('editPrice').value = price.toFixed(2);
        document.getElementById('editStock').value = stock;
    }

    function validateEditForm() {
        const name = document.getElementById('editName').value;
        const price = parseFloat(document.getElementById('editPrice').value);
        const stock = parseInt(document.getElementById('editStock').value);
        if (!name || name.trim() === '') {
            alert('El nombre es requerido');
            return false;
        }
        if (isNaN(price) || price <= 0) {
            alert('El precio debe ser mayor que 0');
            return false;
        }
        if (isNaN(stock) || stock < 0) {
            alert('El stock no puede ser negativo');
            return false;
        }
        return;
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>