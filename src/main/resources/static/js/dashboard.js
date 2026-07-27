let categories = [];
let currentTransactions = [];
let editingTransactionId = null;

window.onload = function() {
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert('로그인이 필요합니다.');
        window.location.href = '/index.html';
        return;
    }

    setUserNameFromJwt(token);
    loadCategories();
    loadTransactions();
};

function setUserNameFromJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );

        const payload = JSON.parse(jsonPayload);
        const name = payload.name || '사용자';
        document.getElementById('userName').textContent = name;
    } catch (e) {
        console.error('JWT 파싱 실패:', e);
    }
}

function getAuthHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
    };
}

async function loadCategories() {
    try {
        const res = await fetch('/api/categories', { headers: getAuthHeaders() });
        const result = await res.json();
        if (res.ok && result.success) {
            categories = result.data || [];
        }
    } catch (err) {
        console.error('카테고리 로드 실패:', err);
    }
}

async function handleCreateCategory(e) {
    e.preventDefault();
    const name = document.getElementById('categoryName').value;
    const type = document.getElementById('categoryType').value;

    try {
        const res = await fetch('/api/categories', {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ name, type })
        });
        const result = await res.json();

        if (res.ok && result.success) {
            alert('카테고리가 추가되었습니다!');
            closeModal('categoryModal');
            document.getElementById('categoryName').value = '';
            loadCategories();
        } else {
            alert(result.message || '카테고리 추가 실패');
        }
    } catch (err) {
        alert('서버 통신 오류가 발생했습니다.');
    }
}

function openTransactionModalForCreate() {
    if (categories.length === 0) {
        alert('등록된 카테고리가 없습니다!\n먼저 [카테고리 추가] 버튼을 눌러 카테고리를 만들어주세요.');
        openModal('categoryModal');
        return;
    }

    editingTransactionId = null;
    document.getElementById('transactionModalTitle').textContent = '새 내역 추가';
    document.getElementById('transactionSubmitBtn').textContent = '저장';

    document.getElementById('transType').value = 'EXPENSE';
    document.getElementById('transAmount').value = '';
    document.getElementById('transDate').value = new Date().toISOString().substring(0, 10);
    document.getElementById('transDescription').value = '';

    filterCategoryDropdown();
    openModal('transactionModal');
}

function openEditModal(id) {
    const item = currentTransactions.find(t => t.id === id);
    if (!item) {
        alert('해당 데이터를 찾을 수 없습니다.');
        return;
    }

    editingTransactionId = id;
    document.getElementById('transactionModalTitle').textContent = '내역 수정';
    document.getElementById('transactionSubmitBtn').textContent = '수정 완료';

    const type = item.type || 'EXPENSE';
    document.getElementById('transType').value = type;

    filterCategoryDropdown();
    if (item.categoryId) {
        document.getElementById('transCategory').value = item.categoryId;
    }

    document.getElementById('transAmount').value = item.amount || 0;
    document.getElementById('transDate').value = formatDate(item.transactionDate || item.createdAt);
    document.getElementById('transDescription').value = item.description || '';

    openModal('transactionModal');
}

function filterCategoryDropdown() {
    const selectedType = document.getElementById('transType').value;
    const categorySelect = document.getElementById('transCategory');
    categorySelect.innerHTML = '';

    const filtered = categories.filter(c => c.type === selectedType);

    if (filtered.length === 0) {
        const option = document.createElement('option');
        option.value = '';
        option.textContent = '해당 유형의 카테고리가 없습니다';
        categorySelect.appendChild(option);
        return;
    }

    filtered.forEach(c => {
        const option = document.createElement('option');
        option.value = c.id;
        option.textContent = c.name;
        categorySelect.appendChild(option);
    });
}

async function handleSaveTransaction(e) {
    e.preventDefault();
    const type = document.getElementById('transType').value;
    const categoryId = document.getElementById('transCategory').value;
    const amount = Number(document.getElementById('transAmount').value);
    const transactionDate = document.getElementById('transDate').value;
    const description = document.getElementById('transDescription').value;

    if (!categoryId) {
        alert('카테고리를 선택해 주세요.');
        return;
    }

    const payload = { categoryId: Number(categoryId), amount, description, transactionDate, type };

    const isEdit = editingTransactionId !== null;
    const url = isEdit ? `/api/transactions/${editingTransactionId}` : '/api/transactions';
    const method = isEdit ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
            headers: getAuthHeaders(),
            body: JSON.stringify(payload)
        });
        const result = await res.json();

        if (res.ok && result.success) {
            alert(isEdit ? '내역이 수정되었습니다!' : '내역이 저장되었습니다!');
            closeModal('transactionModal');
            loadTransactions();
        } else {
            alert(result.message || (isEdit ? '수정 실패' : '추가 실패'));
        }
    } catch (err) {
        alert('서버 통신 오류가 발생했습니다.');
    }
}

async function handleDeleteTransaction(id) {
    if (!confirm('정말 이 내역을 삭제하시겠습니까?')) return;

    try {
        const res = await fetch(`/api/transactions/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        const result = await res.json();

        if (res.ok && result.success) {
            alert('삭제되었습니다.');
            loadTransactions();
        } else {
            alert(result.message || '삭제 실패');
        }
    } catch (err) {
        alert('서버 통신 오류가 발생했습니다.');
    }
}

async function loadTransactions() {
    try {
        const res = await fetch('/api/transactions', { headers: getAuthHeaders() });
        const result = await res.json();

        if (res.ok) {
            let list = [];
            if (Array.isArray(result)) list = result;
            else if (result.data && Array.isArray(result.data)) list = result.data;
            else if (result.content && Array.isArray(result.content)) list = result.content;
            else if (result.data && result.data.content) list = result.data.content;

            currentTransactions = list;
            renderTransactions(list);
        } else {
            document.getElementById('transactionTableBody').innerHTML =
                `<tr><td colspan="6" class="empty-state" style="color:red;">목록 로드 실패: ${result.message || '오류 발생'}</td></tr>`;
        }
    } catch (err) {
        console.error('거래 내역 로드 실패:', err);
        document.getElementById('transactionTableBody').innerHTML =
            '<tr><td colspan="6" class="empty-state" style="color:red;">서버 통신 오류가 발생했습니다.</td></tr>';
    }
}

function formatDate(dateStr) {
    if (!dateStr || dateStr === '-') return '-';
    if (typeof dateStr === 'string' && dateStr.includes('T')) {
        return dateStr.split('T')[0];
    }
    return dateStr;
}

function renderTransactions(list) {
    const tbody = document.getElementById('transactionTableBody');
    tbody.innerHTML = '';

    let totalIncome = 0;
    let totalExpense = 0;

    if (!list || list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-state">등록된 내역이 없습니다. 첫 내역을 추가해보세요!</td></tr>';
        return;
    }

    list.forEach(item => {
        const rawDate = item.transactionDate || item.createdAt || '-';
        const date = formatDate(rawDate);
        const type = item.type || 'EXPENSE';
        const categoryName = item.categoryName || '-';
        const description = item.description || '-';
        const amount = Number(item.amount || 0);

        if (type === 'INCOME') totalIncome += amount;
        else totalExpense += amount;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${date}</td>
            <td class="type-${type}">${type === 'INCOME' ? '수입' : '지출'}</td>
            <td>${categoryName}</td>
            <td>${description}</td>
            <td class="type-${type}">${type === 'INCOME' ? '+' : '-'} ${amount.toLocaleString()}원</td>
            <td>
                <button class="btn-edit" onclick="openEditModal(${item.id})">수정</button>
                <button class="btn-delete" onclick="handleDeleteTransaction(${item.id})">삭제</button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    document.getElementById('totalIncome').textContent = '+ ' + totalIncome.toLocaleString() + ' 원';
    document.getElementById('totalExpense').textContent = '- ' + totalExpense.toLocaleString() + ' 원';

    const balance = totalIncome - totalExpense;
    const balanceEl = document.getElementById('totalBalance');
    balanceEl.textContent = balance.toLocaleString() + ' 원';
    balanceEl.style.color = balance >= 0 ? '#111827' : '#dc2626';
}

function openModal(id) { document.getElementById(id).classList.add('active'); }
function closeModal(id) { document.getElementById(id).classList.remove('active'); }

function handleLogout() {
    localStorage.removeItem('accessToken');
    alert('로그아웃 되었습니다.');
    window.location.href = '/index.html';
}