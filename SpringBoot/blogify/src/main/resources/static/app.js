// Global variables
let currentUser = null;
let authToken = localStorage.getItem("authToken");
let currentSection = "auth";

// API configuration
const API_BASE_URL = "http://localhost:8080/api";

// Initialize the application
document.addEventListener("DOMContentLoaded", function () {
  initializeApp();
  setupEventListeners();
});

function initializeApp() {
  if (authToken) {
    // Try to validate token and get user info
    validateToken();
  } else {
    showLogin();
  }
}

function setupEventListeners() {
  // Auth forms
  document.getElementById("loginForm").addEventListener("submit", handleLogin);
  document
    .getElementById("registerForm")
    .addEventListener("submit", handleRegister);
  document
    .getElementById("createPostForm")
    .addEventListener("submit", handleCreatePost);
}

// Authentication Functions
async function handleLogin(event) {
  event.preventDefault();

  const formData = new FormData(event.target);
  const loginData = {
    username: formData.get("username"),
    password: formData.get("password"),
  };

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    });

    if (response.ok) {
      const data = await response.json();
      authToken = data.token;
      currentUser = {
        username: loginData.username,
        id: data.userId || null,
      };

      localStorage.setItem("authToken", authToken);
      localStorage.setItem("currentUser", JSON.stringify(currentUser));

      showToast("Login successful!", "success");
      updateNavigation(true);
      showHome();
      loadBlogPosts();
    } else {
      const errorData = await response.json();
      showToast(errorData.message || "Login failed", "error");
    }
  } catch (error) {
    console.error("Login error:", error);
    showToast("Login failed. Please try again.", "error");
  } finally {
    showLoading(false);
  }
}

async function handleRegister(event) {
  event.preventDefault();

  const formData = new FormData(event.target);
  const registerData = {
    username: formData.get("username"),
    email: formData.get("email"),
    password: formData.get("password"),
  };

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(registerData),
    });

    if (response.ok) {
      showToast("Registration successful! Please login.", "success");
      showLoginForm();
      document.getElementById("registerForm").reset();
    } else {
      const errorData = await response.json();
      showToast(errorData.message || "Registration failed", "error");
    }
  } catch (error) {
    console.error("Register error:", error);
    showToast("Registration failed. Please try again.", "error");
  } finally {
    showLoading(false);
  }
}

async function validateToken() {
  try {
    const response = await fetch(`${API_BASE_URL}/posts`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const userData = JSON.parse(localStorage.getItem("currentUser"));
      currentUser = userData;
      updateNavigation(true);
      showHome();
      loadBlogPosts();
    } else {
      // Token is invalid
      logout();
    }
  } catch (error) {
    console.error("Token validation error:", error);
    logout();
  }
}

function logout() {
  authToken = null;
  currentUser = null;
  localStorage.removeItem("authToken");
  localStorage.removeItem("currentUser");
  updateNavigation(false);
  showLogin();
  showToast("Logged out successfully", "success");
}

// Navigation Functions
function updateNavigation(isLoggedIn) {
  const loginLink = document.getElementById("login-link");
  const logoutLink = document.getElementById("logout-link");
  const createPostLink = document.getElementById("create-post-link");
  const profileLink = document.getElementById("profile-link");

  if (isLoggedIn) {
    loginLink.style.display = "none";
    logoutLink.style.display = "block";
    createPostLink.style.display = "block";
    profileLink.style.display = "block";
  } else {
    loginLink.style.display = "block";
    logoutLink.style.display = "none";
    createPostLink.style.display = "none";
    profileLink.style.display = "none";
  }
}

function toggleMenu() {
  const navMenu = document.getElementById("nav-menu");
  navMenu.classList.toggle("active");
}

// Section Management Functions
function hideAllSections() {
  document.querySelectorAll(".section").forEach((section) => {
    section.style.display = "none";
  });
}

function showLogin() {
  hideAllSections();
  document.getElementById("auth-section").style.display = "block";
  showLoginForm();
  currentSection = "auth";
}

function showHome() {
  hideAllSections();
  document.getElementById("home-section").style.display = "block";
  currentSection = "home";
  loadBlogPosts();
}

function showCreatePost() {
  if (!authToken) {
    showToast("Please login to create posts", "warning");
    showLogin();
    return;
  }
  hideAllSections();
  document.getElementById("create-post-section").style.display = "block";
  currentSection = "create-post";
}

function showProfile() {
  if (!authToken) {
    showToast("Please login to view profile", "warning");
    showLogin();
    return;
  }
  hideAllSections();
  document.getElementById("profile-section").style.display = "block";
  currentSection = "profile";
  loadUserProfile();
}

function showPostDetail(postId) {
  hideAllSections();
  document.getElementById("post-detail-section").style.display = "block";
  currentSection = "post-detail";
  loadPostDetail(postId);
}

function showLoginForm() {
  document.getElementById("login-form").style.display = "block";
  document.getElementById("register-form").style.display = "none";
  document
    .querySelectorAll(".tab-button")
    .forEach((btn) => btn.classList.remove("active"));
  document.querySelector(".tab-button").classList.add("active");
}

function showRegisterForm() {
  document.getElementById("login-form").style.display = "none";
  document.getElementById("register-form").style.display = "block";
  document
    .querySelectorAll(".tab-button")
    .forEach((btn) => btn.classList.remove("active"));
  document.querySelectorAll(".tab-button")[1].classList.add("active");
}

// Blog Post Functions
async function loadBlogPosts() {
  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/posts`, {
      method: "GET",
      headers: authToken
        ? {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "application/json",
          }
        : {
            "Content-Type": "application/json",
          },
    });

    if (response.ok) {
      const posts = await response.json();
      displayBlogPosts(posts);
    } else {
      showToast("Failed to load blog posts", "error");
    }
  } catch (error) {
    console.error("Error loading blog posts:", error);
    showToast("Failed to load blog posts", "error");
  } finally {
    showLoading(false);
  }
}

function displayBlogPosts(posts) {
  const blogPostsContainer = document.getElementById("blog-posts");

  if (posts.length === 0) {
    blogPostsContainer.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-blog" style="font-size: 3rem; color: #ccc; margin-bottom: 1rem;"></i>
                <h3>No blog posts yet</h3>
                <p>Be the first to create a post!</p>
            </div>
        `;
    return;
  }

  blogPostsContainer.innerHTML = posts
    .map(
      (post) => `
        <div class="blog-post">
            <div class="post-header">
                <div>
                    <h3 class="post-title" onclick="showPostDetail(${
                      post.id
                    })">${escapeHtml(post.title)}</h3>
                    <div class="post-meta">
                        <span><i class="fas fa-user"></i> ${escapeHtml(
                          post.authorName || "Anonymous"
                        )}</span>
                        <span><i class="fas fa-calendar"></i> ${formatDate(
                          post.createdAt
                        )}</span>
                    </div>
                </div>
                ${
                  currentUser && currentUser.username === post.authorName
                    ? `
                    <div class="post-buttons">
                        <button class="btn-small btn-edit" onclick="editPost(${post.id})">
                            <i class="fas fa-edit"></i> Edit
                        </button>
                        <button class="btn-small btn-delete" onclick="deletePost(${post.id})">
                            <i class="fas fa-trash"></i> Delete
                        </button>
                    </div>
                `
                    : ""
                }
            </div>
            <div class="post-content">
                ${truncateText(escapeHtml(post.content), 200)}
            </div>
            <div class="post-actions">
                <div class="post-stats">
                    <span><i class="fas fa-comments"></i> ${
                      post.commentCount || 0
                    } comments</span>
                </div>
                <button class="btn-small" onclick="showPostDetail(${
                  post.id
                })" style="background: #667eea; color: white;">
                    <i class="fas fa-eye"></i> Read More
                </button>
            </div>
        </div>
    `
    )
    .join("");
}

async function handleCreatePost(event) {
  event.preventDefault();

  if (!authToken) {
    showToast("Please login to create posts", "warning");
    showLogin();
    return;
  }

  const formData = new FormData(event.target);
  const postData = {
    title: formData.get("title"),
    content: formData.get("content"),
  };

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/posts`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postData),
    });

    if (response.ok) {
      showToast("Post created successfully!", "success");
      document.getElementById("createPostForm").reset();
      showHome();
      loadBlogPosts();
    } else {
      const errorData = await response.json();
      showToast(errorData.message || "Failed to create post", "error");
    }
  } catch (error) {
    console.error("Error creating post:", error);
    showToast("Failed to create post", "error");
  } finally {
    showLoading(false);
  }
}

async function deletePost(postId) {
  if (!confirm("Are you sure you want to delete this post?")) {
    return;
  }

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/posts/${postId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      showToast("Post deleted successfully!", "success");
      loadBlogPosts();
    } else {
      showToast("Failed to delete post", "error");
    }
  } catch (error) {
    console.error("Error deleting post:", error);
    showToast("Failed to delete post", "error");
  } finally {
    showLoading(false);
  }
}

// Post Detail Functions
async function loadPostDetail(postId) {
  try {
    showLoading(true);

    // Load post details
    const postResponse = await fetch(`${API_BASE_URL}/posts/${postId}`, {
      method: "GET",
      headers: authToken
        ? {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "application/json",
          }
        : {
            "Content-Type": "application/json",
          },
    });

    if (postResponse.ok) {
      const post = await postResponse.json();

      // Load comments
      const commentsResponse = await fetch(
        `${API_BASE_URL}/comments/posts/${postId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const comments = commentsResponse.ok ? await commentsResponse.json() : [];

      displayPostDetail(post, comments);
    } else {
      showToast("Failed to load post details", "error");
      showHome();
    }
  } catch (error) {
    console.error("Error loading post detail:", error);
    showToast("Failed to load post details", "error");
    showHome();
  } finally {
    showLoading(false);
  }
}

function displayPostDetail(post, comments) {
  const postDetailContainer = document.getElementById("post-detail");

  postDetailContainer.innerHTML = `
        <div class="post-detail">
            <div class="post-header">
                <h1 class="post-title">${escapeHtml(post.title)}</h1>
                <div class="post-meta">
                    <span><i class="fas fa-user"></i> ${escapeHtml(
                      post.authorName || "Anonymous"
                    )}</span>
                    <span><i class="fas fa-calendar"></i> ${formatDate(
                      post.createdAt
                    )}</span>
                    ${
                      post.updatedAt && post.updatedAt !== post.createdAt
                        ? `<span><i class="fas fa-edit"></i> Updated ${formatDate(
                            post.updatedAt
                          )}</span>`
                        : ""
                    }
                </div>
            </div>
            <div class="post-content">${escapeHtml(post.content).replace(
              /\n/g,
              "<br>"
            )}</div>
            
            <div class="post-actions">
                <button class="btn-secondary" onclick="showHome()">
                    <i class="fas fa-arrow-left"></i> Back to Posts
                </button>
                ${
                  currentUser && currentUser.username === post.authorName
                    ? `
                    <div class="post-buttons">
                        <button class="btn-small btn-edit" onclick="editPost(${post.id})">
                            <i class="fas fa-edit"></i> Edit
                        </button>
                        <button class="btn-small btn-delete" onclick="deletePost(${post.id})">
                            <i class="fas fa-trash"></i> Delete
                        </button>
                    </div>
                `
                    : ""
                }
            </div>
        </div>

        <div class="comments-section">
            <div class="comments-header">
                <h3><i class="fas fa-comments"></i> Comments (${
                  comments.length
                })</h3>
            </div>

            ${
              authToken
                ? `
                <div class="comment-form">
                    <form id="commentForm" onsubmit="handleCreateComment(event, ${post.id})">
                        <div class="form-group">
                            <textarea name="content" placeholder="Write your comment..." required></textarea>
                        </div>
                        <button type="submit" class="btn-primary">
                            <i class="fas fa-paper-plane"></i> Post Comment
                        </button>
                    </form>
                </div>
            `
                : `
                <div class="comment-form">
                    <p style="text-align: center; color: #666; padding: 2rem;">
                        <i class="fas fa-sign-in-alt"></i> 
                        <a href="#" onclick="showLogin()" style="color: #667eea;">Login</a> to post comments
                    </p>
                </div>
            `
            }

            <div class="comments-list">
                ${
                  comments.length === 0
                    ? `
                    <div class="empty-state" style="text-align: center; padding: 2rem; color: #666;">
                        <i class="fas fa-comments" style="font-size: 2rem; margin-bottom: 1rem; opacity: 0.5;"></i>
                        <p>No comments yet. Be the first to comment!</p>
                    </div>
                `
                    : comments
                        .map(
                          (comment) => `
                    <div class="comment">
                        <div class="comment-header">
                            <div class="comment-author">
                                <i class="fas fa-user-circle"></i> ${escapeHtml(
                                  comment.authorName
                                )}
                            </div>
                            <div class="comment-date">${formatDate(
                              comment.createdAt
                            )}</div>
                        </div>
                        <div class="comment-content">${escapeHtml(
                          comment.content
                        ).replace(/\n/g, "<br>")}</div>
                        ${
                          currentUser &&
                          currentUser.username === comment.authorName
                            ? `
                            <div class="comment-actions" style="margin-top: 1rem;">
                                <button class="btn-small btn-delete" onclick="deleteComment(${comment.id}, ${post.id})">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </div>
                        `
                            : ""
                        }
                    </div>
                `
                        )
                        .join("")
                }
            </div>
        </div>
    `;
}

// Comment Functions
async function handleCreateComment(event, postId) {
  event.preventDefault();

  if (!authToken) {
    showToast("Please login to comment", "warning");
    showLogin();
    return;
  }

  const formData = new FormData(event.target);
  const commentData = {
    content: formData.get("content"),
  };

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/comments/posts/${postId}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(commentData),
    });

    if (response.ok) {
      showToast("Comment posted successfully!", "success");
      event.target.reset();
      loadPostDetail(postId); // Reload to show new comment
    } else {
      const errorData = await response.json();
      showToast(errorData.message || "Failed to post comment", "error");
    }
  } catch (error) {
    console.error("Error creating comment:", error);
    showToast("Failed to post comment", "error");
  } finally {
    showLoading(false);
  }
}

async function deleteComment(commentId, postId) {
  if (!confirm("Are you sure you want to delete this comment?")) {
    return;
  }

  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/comments/${commentId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      showToast("Comment deleted successfully!", "success");
      loadPostDetail(postId); // Reload to update comments
    } else {
      showToast("Failed to delete comment", "error");
    }
  } catch (error) {
    console.error("Error deleting comment:", error);
    showToast("Failed to delete comment", "error");
  } finally {
    showLoading(false);
  }
}

// Profile Functions
async function loadUserProfile() {
  // For now, just display basic user info
  const userInfoContainer = document.getElementById("user-info");
  const userPostsContainer = document.getElementById("user-posts-list");

  userInfoContainer.innerHTML = `
        <div style="text-align: center;">
            <div style="font-size: 4rem; color: #667eea; margin-bottom: 1rem;">
                <i class="fas fa-user-circle"></i>
            </div>
            <h2>${escapeHtml(currentUser.username)}</h2>
            <p style="color: #666;">Member since ${new Date().toLocaleDateString()}</p>
        </div>
    `;

  // Load user's posts
  try {
    showLoading(true);
    const response = await fetch(`${API_BASE_URL}/posts`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const allPosts = await response.json();
      const userPosts = allPosts.filter(
        (post) => post.authorName === currentUser.username
      );

      if (userPosts.length === 0) {
        userPostsContainer.innerHTML = `
                    <div class="empty-state" style="text-align: center; padding: 2rem;">
                        <i class="fas fa-pen" style="font-size: 2rem; color: #ccc; margin-bottom: 1rem;"></i>
                        <p>You haven't created any posts yet.</p>
                        <button class="btn-primary" onclick="showCreatePost()">
                            <i class="fas fa-plus"></i> Create Your First Post
                        </button>
                    </div>
                `;
      } else {
        userPostsContainer.innerHTML = userPosts
          .map(
            (post) => `
                    <div class="blog-post">
                        <h4 class="post-title" onclick="showPostDetail(${
                          post.id
                        })">${escapeHtml(post.title)}</h4>
                        <div class="post-meta">
                            <span><i class="fas fa-calendar"></i> ${formatDate(
                              post.createdAt
                            )}</span>
                            <span><i class="fas fa-comments"></i> ${
                              post.commentCount || 0
                            } comments</span>
                        </div>
                        <div class="post-content">${truncateText(
                          escapeHtml(post.content),
                          150
                        )}</div>
                        <div class="post-actions">
                            <button class="btn-small" onclick="showPostDetail(${
                              post.id
                            })" style="background: #667eea; color: white;">
                                <i class="fas fa-eye"></i> View
                            </button>
                            <button class="btn-small btn-edit" onclick="editPost(${
                              post.id
                            })">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn-small btn-delete" onclick="deletePost(${
                              post.id
                            })">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </div>
                    </div>
                `
          )
          .join("");
      }
    }
  } catch (error) {
    console.error("Error loading user posts:", error);
    userPostsContainer.innerHTML = "<p>Failed to load your posts.</p>";
  } finally {
    showLoading(false);
  }
}

// Utility Functions
function showLoading(show) {
  const loading = document.getElementById("loading");
  loading.style.display = show ? "block" : "none";
}

function showToast(message, type = "info") {
  const toastContainer = document.getElementById("toast-container");
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerHTML = `
        <div style="display: flex; align-items: center; justify-content: space-between;">
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" style="background: none; border: none; font-size: 1.2rem; cursor: pointer; margin-left: 1rem;">&times;</button>
        </div>
    `;

  toastContainer.appendChild(toast);

  setTimeout(() => {
    if (toast.parentElement) {
      toast.remove();
    }
  }, 5000);
}

function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

function truncateText(text, maxLength) {
  if (text.length <= maxLength) return text;
  return text.substr(0, maxLength) + "...";
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return (
    date.toLocaleDateString() +
    " " +
    date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })
  );
}

function editPost(postId) {
  // For simplicity, we'll just show an alert for now
  // In a real app, you'd create an edit form
  showToast("Edit functionality coming soon!", "warning");
}
