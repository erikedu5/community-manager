INSERT INTO public.ciudadanos
(id,activo, curp, descripción, direccion, fecha_actualizacion, fecha_creacion, fecha_nacimiento, nombre, usuario_editor)
VALUES(0, true, 'AAAA000000HHHHHH00', NULL, NULL, NULL, now(), now(), 'Administrador de sistema', null);

INSERT INTO public.comitivas
(id, activo, fecha_actualizacion, fecha_creacion, nombre, usuario_editor)
VALUES(0, true, null, now(), 'Administrador', null);


INSERT INTO public.administradores
(id, activo, fecha_actualizacion, fecha_creacion, "password", "role", user_name, usuario_editor, ciudadano_id, comitiva_id)
VALUES(0, true, null, now(), '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PRESIDENTE', 'admin', null, 0, 0);
