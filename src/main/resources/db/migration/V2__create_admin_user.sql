INSERT INTO users (nome, email, senha, role)
SELECT
    'Administrador',
    'admin@admin.com',
    '$2a$10$sxnu0/1ANheNMvMqqRGACuduu/0XAnxQXWU8wcqhdiiYXTbm2K3Gi',
    'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@admin.com'
);

