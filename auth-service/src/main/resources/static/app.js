const apiBase = '/auth';

const state = {
  users: []
};

const elements = {
  serviceState: document.getElementById('serviceState'),
  proxyState: document.getElementById('proxyState'),
  tokenState: document.getElementById('tokenState'),
  authUserCount: document.getElementById('authUserCount'),
  usersTable: document.getElementById('usersTable'),
  responseBox: document.getElementById('responseBox'),
  refreshBtn: document.getElementById('refreshBtn'),
  searchInput: document.getElementById('searchInput'),
  loginForm: document.getElementById('loginForm'),
  tokenForm: document.getElementById('tokenForm'),
  lookupForm: document.getElementById('lookupForm'),
  loginUsername: document.getElementById('loginUsername'),
  loginPassword: document.getElementById('loginPassword'),
  tokenInput: document.getElementById('tokenInput'),
  lookupId: document.getElementById('lookupId')
};

function escapeHtml(value) {
  return String(value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function showResponse(payload, title = 'Response') {
  const body = typeof payload === 'string' ? payload : JSON.stringify(payload, null, 2);
  elements.responseBox.innerHTML = `
    <p class="mini-title">${escapeHtml(title)}</p>
    <pre class="response-pre">${escapeHtml(body)}</pre>
  `;
}

function setState(text, className = 'status-warn') {
  elements.serviceState.textContent = text;
  elements.serviceState.className = `pill ${className === 'status-ok' ? 'pill-live' : ''}`.trim();
}

function setTokenState(text, className) {
  elements.tokenState.textContent = text;
  elements.tokenState.className = `stat-value ${className}`;
}

function setProxyState(text, className) {
  elements.proxyState.textContent = text;
  elements.proxyState.className = `stat-value ${className}`;
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  });

  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.text();

  if (!response.ok) {
    throw new Error(typeof body === 'string' ? body : JSON.stringify(body));
  }

  return body;
}

function normalizeUser(user) {
  return {
    id: user.id ?? '',
    username: user.username ?? '',
    password: user.password ?? '',
    enabled: user.enabled ? 'Enabled' : 'Disabled'
  };
}

function renderUsers() {
  const query = elements.searchInput.value.trim().toLowerCase();
  const filtered = state.users.filter(user => {
    const haystack = `${user.id} ${user.username} ${user.password} ${user.enabled}`.toLowerCase();
    return haystack.includes(query);
  });

  elements.authUserCount.textContent = String(state.users.length);

  if (filtered.length === 0) {
    elements.usersTable.innerHTML = `
      <tr>
        <td colspan="4" class="empty-row">No auth users found.</td>
      </tr>
    `;
    return;
  }

  elements.usersTable.innerHTML = filtered.map(user => `
    <tr>
      <td>${escapeHtml(user.id)}</td>
      <td>${escapeHtml(user.username)}</td>
      <td>${escapeHtml(user.password)}</td>
      <td>${escapeHtml(user.enabled)}</td>
    </tr>
  `).join('');
}

async function loadAuthUsers() {
  try {
    const users = await request(`${apiBase}/users`);
    state.users = Array.isArray(users) ? users.map(normalizeUser) : [];
    setState('Service online', 'status-ok');
    renderUsers();
  } catch (error) {
    state.users = [];
    setState('Service offline', 'status-bad');
    renderUsers();
    showResponse(`Unable to load auth users: ${error.message}`, 'Error');
  }
}

async function checkUserProxy() {
  try {
    const profile = await request(`${apiBase}/users/1`);
    setProxyState('Proxy ready', 'status-ok');
    return profile;
  } catch (error) {
    setProxyState('Proxy down', 'status-bad');
    return null;
  }
}

async function validateToken(token) {
  const result = await request(`${apiBase}/validate-token?token=${encodeURIComponent(token)}`);
  setTokenState(result.valid ? 'Valid' : 'Invalid', result.valid ? 'status-ok' : 'status-bad');
  showResponse(result, 'Token validation');
}

elements.loginForm.addEventListener('submit', async event => {
  event.preventDefault();
  const username = elements.loginUsername.value.trim();
  const password = elements.loginPassword.value.trim();

  if (!username || !password) {
    showResponse('Please enter a username and password.', 'Validation');
    return;
  }

  try {
    const result = await request(`${apiBase}/login`, {
      method: 'POST',
      body: JSON.stringify({ username, password })
    });
    setTokenState('Logged in', 'status-ok');
    showResponse(result, 'Login result');
  } catch (error) {
    setTokenState('Login failed', 'status-bad');
    showResponse(`Login failed: ${error.message}`, 'Error');
  }
});

elements.tokenForm.addEventListener('submit', async event => {
  event.preventDefault();
  const token = elements.tokenInput.value.trim();

  if (!token) {
    showResponse('Enter a token first.', 'Validation');
    return;
  }

  try {
    await validateToken(token);
  } catch (error) {
    setTokenState('Invalid', 'status-bad');
    showResponse(`Token check failed: ${error.message}`, 'Error');
  }
});

elements.lookupForm.addEventListener('submit', async event => {
  event.preventDefault();
  const id = elements.lookupId.value.trim();

  if (!id) {
    showResponse('Enter a user ID first.', 'Validation');
    return;
  }

  try {
    const profile = await request(`${apiBase}/users/${encodeURIComponent(id)}`);
    showResponse(profile, `User profile #${id}`);
  } catch (error) {
    showResponse(`Unable to fetch user profile: ${error.message}`, 'Error');
  }
});

elements.refreshBtn.addEventListener('click', async () => {
  elements.refreshBtn.disabled = true;
  elements.refreshBtn.textContent = 'Refreshing...';
  try {
    await loadAuthUsers();
    await checkUserProxy();
  } finally {
    elements.refreshBtn.disabled = false;
    elements.refreshBtn.textContent = 'Refresh data';
  }
});

elements.searchInput.addEventListener('input', renderUsers);

Promise.all([
  loadAuthUsers(),
  checkUserProxy(),
  validateToken('token-alice-123')
]).catch(error => {
  showResponse(`Startup check failed: ${error.message}`, 'Error');
});
