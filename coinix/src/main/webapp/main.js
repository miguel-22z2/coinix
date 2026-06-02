// Lê parâmetros da URL e exibe alertas
document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);

    const msgErro = document.getElementById('msg-erro');
    const msgOk   = document.getElementById('msg-ok');
    const msgErroEmail  = document.getElementById('msg-erro-email');
    const msgErroGeral  = document.getElementById('msg-erro-geral');

    if (params.get('erro') === '1' && msgErro)   msgErro.style.display = 'block';
    if (params.get('cadastro') === 'ok' && msgOk) msgOk.style.display = 'block';
    if (params.get('erro') === 'email' && msgErroEmail) msgErroEmail.style.display = 'block';
    if (params.get('erro') === 'geral' && msgErroGeral) msgErroGeral.style.display = 'block';

    // Filtro de busca na tabela
    const busca = document.getElementById('busca');
    if (busca) {
        busca.addEventListener('input', () => {
            const termo = busca.value.toLowerCase();
            const linhas = document.querySelectorAll('#tabela-despesas tbody tr');
            linhas.forEach(tr => {
                tr.style.display = tr.textContent.toLowerCase().includes(termo) ? '' : 'none';
            });
        });
    }

    // Define a data de hoje nos inputs de data
    const inputsData = document.querySelectorAll('input[type="date"]');
    const hoje = new Date().toISOString().split('T')[0];
    inputsData.forEach(input => {
        if (!input.value) input.value = hoje;
    });
});