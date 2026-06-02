<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.UsuarioBean, model.DespesaBean, dao.DespesaDAO, java.util.List, java.math.BigDecimal" %>
<%
    UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/index.html");
        return;
    }

    DespesaDAO dao = new DespesaDAO();
    List<DespesaBean> despesas = dao.listar(usuario.getId());

    BigDecimal total = BigDecimal.ZERO;
    for (DespesaBean d : despesas) total = total.add(d.getValor());

    String editId = request.getParameter("edit");
    DespesaBean editDespesa = null;
    if (editId != null) {
        editDespesa = dao.buscarPorId(usuario.getId(), Integer.parseInt(editId));
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinançaFácil — Dashboard</title>
    <link rel="stylesheet" href="dashboard.css">
    
</head>
<body>

    <!-- Header -->
    <div class="header">
        <h1 style="margin:0; font-size:1.3em;">Coinix</h1>
        <div style="display:flex; align-items:center; gap:16px; color:#aaa; font-size:.9em;">
            <strong style="color:#fff;"><%= usuario.getNome().split(" ")[0] %></strong>
            <a href="logout" class="Botao2" style="font-size:.85em; padding:4px 12px;">Sair</a>
        </div>
    </div>

    <div class="container">

        <!-- Cards de resumo -->
        <div class="cards-resumo">
            <div class="card-resumo">
                <div class="card-resumo-label">Total de Gastos</div>
                <div class="card-resumo-valor">R$ <%= String.format("%.2f", total) %></div>
            </div>
            <div class="card-resumo" style="border-left-color: #00cc66;">
                <div class="card-resumo-label">Lançamentos</div>
                <div class="card-resumo-valor" style="color:#00cc66;"><%= despesas.size() %></div>
            </div>
        </div>

        <!-- Formulário de nova despesa -->
        <div class="section-box">
            <h1 style="font-size:1.1em; margin-bottom:16px;">Nova Despesa</h1>
            <form action="despesas" method="post">
                <input type="hidden" name="acao" value="inserir">
                <div class="form-row">
                    <div class="form-col">
                        <label>Descrição</label>
                        <input class="Nome" type="text" name="descricao" required placeholder="Ex: Supermercado">
                    </div>
                    <div class="form-col">
                        <label>Valor (R$)</label>
                        <input class="Valor" type="number" name="valor" step="0.01" min="0.01" required placeholder="0,00">
                    </div>
                    <div class="form-col">
                        <label>Categoria</label>
                        <select class="Valor" name="categoria" required>
                            <option value="">Selecione...</option>
                            <option value="Alimentação">Alimentação</option>
                            <option value="Transporte">Transporte</option>
                            <option value="Saúde">Saúde</option>
                            <option value="Educação">Educação</option>
                            <option value="Lazer">Lazer</option>
                            <option value="Moradia">Moradia</option>
                            <option value="Vestuário">Vestuário</option>
                            <option value="Outros">Outros</option>
                        </select>
                    </div>
                    <div class="form-col">
                        <label>Data</label>
                        <input class="Data" type="date" name="data" required>
                    </div>
                    <div style="display:flex; align-items:flex-end;">
                        <button type="submit" class="Botao1">+ Adicionar</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Tabela de despesas -->
        <div class="section-box">
            <div class="table-header-row">
                <h1 style="font-size:1.1em; margin:0;">Minhas Despesas</h1>
                <p>
            </div>

            <% if (despesas.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-icon">📋</div>
                    <p>Nenhuma despesa cadastrada ainda.<br></p>
                </div>
            <% } else { %>
                <div style="overflow-x:auto;">
                    <table id="tabela" style="width:100%; border-collapse:collapse;">
                        <thead>
                            <tr style="background:#1e1e1e;">
                                <th>Descrição</th>
                                <th>Categoria</th>
                                <th>Data</th>
                                <th>Valor</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (DespesaBean d : despesas) { %>
                            <tr>
                                <td><%= d.getDescricao() %></td>
                                <td><%= d.getCategoria() %></td>
                                <td><%= d.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></td>
                                <td style="color:#ff6666; font-weight:700;">
                                    R$ <%= String.format("%.2f", d.getValor()) %>
                                </td>
                                <td style="white-space:nowrap; display:flex; gap:6px;">
                                    <a href="dashboard.jsp?edit=<%= d.getId() %>" class="Botao1"
                                       style="font-size:.85em; padding:4px 10px;">✏️ Editar</a>

                                    <form action="despesas" method="post" style="display:inline"
                                          onsubmit="return confirm('Excluir esta despesa?')">
                                        <input type="hidden" name="acao" value="excluir">
                                        <input type="hidden" name="id" value="<%= d.getId() %>">
                                        <button type="submit" class="Botao2"
                                                style="font-size:.85em; padding:4px 10px;">🗑️ Excluir</button>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>

    </div><!-- /container -->

    <!-- Modal de edição -->
    <% if (editDespesa != null) { %>
    <div class="modal-overlay">
        <div class="modal-box">
            <div class="modal-header">
                <h1 style="font-size:1.1em; margin:0;">✏️ Editar Despesa</h1>
                <a href="dashboard.jsp" class="modal-close">✕</a>
            </div>
            <form action="despesas" method="post">
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="id" value="<%= editDespesa.getId() %>">

                <label style="font-size:.82em; color:#66bbff; font-weight:600;">Descrição</label><br>
                <input class="Nome" type="text" name="descricao" required
                       value="<%= editDespesa.getDescricao() %>"
                       style="background:#1e1e1e; color:#fff;"><br>

                <label style="font-size:.82em; color:#66bbff; font-weight:600;">Valor (R$)</label><br>
                <input class="Valor" type="number" name="valor" step="0.01" required
                       value="<%= editDespesa.getValor() %>"
                       style="background:#1e1e1e; color:#fff;"><br>

                <label style="font-size:.82em; color:#66bbff; font-weight:600;">Categoria</label><br>
                <select class="Valor" name="categoria" required style="background:#1e1e1e; color:#fff;">
                    <% String[] cats = {"Alimentação","Transporte","Saúde","Educação","Lazer","Moradia","Vestuário","Outros"};
                       for (String c : cats) { %>
                        <option value="<%= c %>" <%= c.equals(editDespesa.getCategoria()) ? "selected" : "" %>><%= c %></option>
                    <% } %>
                </select><br>

                <label style="font-size:.82em; color:#66bbff; font-weight:600;">Data</label><br>
                <input class="Data" type="date" name="data" required
                       value="<%= editDespesa.getData() %>"
                       style="background:#1e1e1e; color:#fff;"><br>

                <div class="modal-footer">
                    <a href="dashboard.jsp" class="btn-cancelar">Cancelar</a>
                    <button type="submit" class="Botao1">Salvar</button>
                </div>
            </form>
        </div>
    </div>
    <% } %>

    <script src="js/main.js"></script>
</body>
</html>