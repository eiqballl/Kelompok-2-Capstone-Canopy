import os
import re

print("Patching DireksiFrame and PetugasFrame...")

# --------------------------------------------------------------------------
# A. Patch DireksiFrame.form
# --------------------------------------------------------------------------
with open('src/main/java/view/DireksiFrame.form', 'r', encoding='utf-8') as f:
    df_form = f.read()

# 1. Tab titles without numbers
df_form = df_form.replace(
    '<JTabbedPaneConstraints tabName="1. Rekapitulasi Hasil Panen">\n                <Property name="tabTitle" type="java.lang.String" value="1. Rekapitulasi Hasil Panen"/>',
    '<JTabbedPaneConstraints tabName="Rekapitulasi">\n                <Property name="tabTitle" type="java.lang.String" value="Rekapitulasi"/>'
)
df_form = df_form.replace(
    '<JTabbedPaneConstraints tabName="2. Evaluasi Hasil Panen">\n                <Property name="tabTitle" type="java.lang.String" value="2. Evaluasi Hasil Panen"/>',
    '<JTabbedPaneConstraints tabName="Evaluasi Panen">\n                <Property name="tabTitle" type="java.lang.String" value="Evaluasi Panen"/>'
)

# 2. splitEval dividerLocation to 340
df_form = re.sub(
    r'(<Container class="javax.swing.JSplitPane" name="splitEval">\s*<Properties>\s*<Property name="dividerLocation" type="int" value=")420("/>)',
    r'\g<1>340\g<2>',
    df_form
)

# 3. Filter Mutu and buttons in pnlFilterRekap
df_form = df_form.replace(
    '<Property name="text" type="java.lang.String" value="Filter Blok:"/>',
    '<Property name="text" type="java.lang.String" value="Filter Mutu Panen:"/>'
)

old_df_dim0 = """                              <Group type="103" groupAlignment="0" attributes="0">
                                  <Component id="cmbModeRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="cmbFilterKomoditas" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAwal" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAkhir" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnTampilkanRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnResetRekap" alignment="0" max="32767" attributes="0"/>
                                  <Group type="102" alignment="0" attributes="0">
                                      <Group type="103" groupAlignment="0" attributes="0">
                                          <Component id="lblModeRekap" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblFilterKomoditas" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAwal" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAkhir" alignment="0" min="-2" max="-2" attributes="0"/>
                                      </Group>
                                      <EmptySpace min="0" pref="0" max="32767" attributes="0"/>
                                  </Group>
                              </Group>"""

new_df_dim0 = """                              <Group type="103" groupAlignment="0" attributes="0">
                                  <Component id="cmbModeRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="cmbFilterKomoditas" alignment="0" max="32767" attributes="0"/>
                                  <Component id="cmbFilterBlok" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnPilihBlok" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAwal" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAkhir" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnTampilkanRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnRangkumanRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnResetRekap" alignment="0" max="32767" attributes="0"/>
                                  <Group type="102" alignment="0" attributes="0">
                                      <Group type="103" groupAlignment="0" attributes="0">
                                          <Component id="lblModeRekap" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblFilterKomoditas" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblFilterBlok" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAwal" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAkhir" alignment="0" min="-2" max="-2" attributes="0"/>
                                      </Group>
                                      <EmptySpace min="0" pref="0" max="32767" attributes="0"/>
                                  </Group>
                              </Group>"""

if old_df_dim0 in df_form:
    df_form = df_form.replace(old_df_dim0, new_df_dim0)

old_df_dim1 = """                              <Component id="lblModeRekap" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="cmbModeRekap" min="-2" pref="34" max="-2" attributes="0"/>
                              <EmptySpace min="-2" pref="18" max="-2" attributes="0"/>
                              <Component id="lblFilterKomoditas" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="cmbFilterKomoditas" min="-2" pref="34" max="-2" attributes="0"/>
                              <EmptySpace min="-2" pref="18" max="-2" attributes="0"/>
                              <Component id="lblTglAwal" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="txtTglAwal" min="-2" pref="34" max="-2" attributes="0"/>
                              <EmptySpace min="-2" pref="18" max="-2" attributes="0"/>
                              <Component id="lblTglAkhir" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="txtTglAkhir" min="-2" pref="34" max="-2" attributes="0"/>
                              <EmptySpace min="-2" pref="28" max="-2" attributes="0"/>
                              <Component id="btnTampilkanRekap" min="-2" pref="42" max="-2" attributes="0"/>
                              <EmptySpace type="unrelated" max="-2" attributes="0"/>
                              <Component id="btnResetRekap" min="-2" pref="36" max="-2" attributes="0"/>"""

new_df_dim1 = """                              <Component id="lblModeRekap" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="cmbModeRekap" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="lblFilterKomoditas" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="cmbFilterKomoditas" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="lblFilterBlok" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="cmbFilterBlok" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="btnPilihBlok" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="lblTglAwal" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="txtTglAwal" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="lblTglAkhir" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="txtTglAkhir" min="-2" pref="30" max="-2" attributes="0"/>
                              <EmptySpace type="unrelated" max="-2" attributes="0"/>
                              <Component id="btnTampilkanRekap" min="-2" pref="32" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="btnRangkumanRekap" min="-2" pref="32" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="btnResetRekap" min="-2" pref="32" max="-2" attributes="0"/>"""

if old_df_dim1 in df_form:
    df_form = df_form.replace(old_df_dim1, new_df_dim1)

if 'name="btnPilihBlok"' not in df_form:
    sub_df_btns = """                    <Component class="javax.swing.JButton" name="btnPilihBlok">
                      <Properties>
                        <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                          <Font name="Segoe UI" size="12" style="0"/>
                        </Property>
                        <Property name="text" type="java.lang.String" value="Pilih Blok Lahan (Semua)"/>
                      </Properties>
                      <Events>
                        <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="btnPilihBlokActionPerformed"/>
                      </Events>
                    </Component>
                    <Component class="javax.swing.JButton" name="btnRangkumanRekap">
                      <Properties>
                        <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                          <Color blue="4a" green="a3" red="16" type="rgb"/>
                        </Property>
                        <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                          <Font name="Segoe UI" size="12" style="1"/>
                        </Property>
                        <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                          <Color blue="ff" green="ff" red="ff" type="rgb"/>
                        </Property>
                        <Property name="text" type="java.lang.String" value="Lihat Rangkuman Eksekutif"/>
                      </Properties>
                      <Events>
                        <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="btnRangkumanRekapActionPerformed"/>
                      </Events>
                    </Component>"""
    df_form = df_form.replace('<Component class="javax.swing.JButton" name="btnTampilkanRekap">', sub_df_btns + '\n                    <Component class="javax.swing.JButton" name="btnTampilkanRekap">')

with open('src/main/java/view/DireksiFrame.form', 'w', encoding='utf-8') as f:
    f.write(df_form)
print("DireksiFrame.form patched!")

# --------------------------------------------------------------------------
# B. Patch PetugasFrame.form
# --------------------------------------------------------------------------
with open('src/main/java/view/PetugasFrame.form', 'r', encoding='utf-8') as f:
    pf_form = f.read()

# 1. Tab titles without numbers
pf_form = pf_form.replace(
    '<JTabbedPaneConstraints tabName="1. Kelola Data Panen">\n                <Property name="tabTitle" type="java.lang.String" value="1. Kelola Data Panen"/>',
    '<JTabbedPaneConstraints tabName="Input Panen">\n                <Property name="tabTitle" type="java.lang.String" value="Input Panen"/>'
)
pf_form = pf_form.replace(
    '<JTabbedPaneConstraints tabName="2. Rekapitulasi Hasil Panen">\n                <Property name="tabTitle" type="java.lang.String" value="2. Rekapitulasi Hasil Panen"/>',
    '<JTabbedPaneConstraints tabName="Rekapitulasi">\n                <Property name="tabTitle" type="java.lang.String" value="Rekapitulasi"/>'
)
pf_form = pf_form.replace(
    '<JTabbedPaneConstraints tabName="3. Evaluasi Hasil Panen">\n                <Property name="tabTitle" type="java.lang.String" value="3. Evaluasi Hasil Panen"/>',
    '<JTabbedPaneConstraints tabName="Evaluasi Panen">\n                <Property name="tabTitle" type="java.lang.String" value="Evaluasi Panen"/>'
)

# 2. Rekapitulasi filter layout
pf_form = pf_form.replace(
    '<Property name="text" type="java.lang.String" value="Filter Blok:"/>',
    '<Property name="text" type="java.lang.String" value="Filter Mutu Panen:"/>'
)

if old_df_dim0 in pf_form:
    pf_form = pf_form.replace(old_df_dim0, new_df_dim0)

if old_df_dim1 in pf_form:
    pf_form = pf_form.replace(old_df_dim1, new_df_dim1)

if 'name="btnPilihBlok"' not in pf_form:
    pf_form = pf_form.replace('<Component class="javax.swing.JButton" name="btnTampilkanRekap">', sub_df_btns + '\n                    <Component class="javax.swing.JButton" name="btnTampilkanRekap">')

# 3. Add btnLaporEval in pnlTabEvaluasi
old_eval_head = """                          <Group type="102" attributes="0">
                              <Component id="lblDaftarEval" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="32767" attributes="0"/>
                              <Component id="btnRefreshEval" min="-2" pref="140" max="-2" attributes="0"/>
                          </Group>"""

new_eval_head = """                          <Group type="102" attributes="0">
                              <Component id="lblDaftarEval" min="-2" max="-2" attributes="0"/>
                              <EmptySpace max="32767" attributes="0"/>
                              <Component id="btnLaporEval" min="-2" pref="180" max="-2" attributes="0"/>
                              <EmptySpace max="-2" attributes="0"/>
                              <Component id="btnRefreshEval" min="-2" pref="130" max="-2" attributes="0"/>
                          </Group>"""

if old_eval_head in pf_form:
    pf_form = pf_form.replace(old_eval_head, new_eval_head)

# In vertical layout for pnlTabEvaluasi
old_eval_v = """                      <Group type="103" groupAlignment="3" attributes="0">
                          <Component id="lblDaftarEval" alignment="3" min="-2" max="-2" attributes="0"/>
                          <Component id="btnRefreshEval" alignment="3" min="-2" pref="34" max="-2" attributes="0"/>
                      </Group>"""

new_eval_v = """                      <Group type="103" groupAlignment="3" attributes="0">
                          <Component id="lblDaftarEval" alignment="3" min="-2" max="-2" attributes="0"/>
                          <Component id="btnLaporEval" alignment="3" min="-2" pref="34" max="-2" attributes="0"/>
                          <Component id="btnRefreshEval" alignment="3" min="-2" pref="34" max="-2" attributes="0"/>
                      </Group>"""

if old_eval_v in pf_form:
    pf_form = pf_form.replace(old_eval_v, new_eval_v)

if 'name="btnLaporEval"' not in pf_form:
    sub_lapor = """            <Component class="javax.swing.JButton" name="btnLaporEval">
              <Properties>
                <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="4a" green="a3" red="16" type="rgb"/>
                </Property>
                <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                  <Font name="Segoe UI" size="13" style="1"/>
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="ff" green="ff" red="ff" type="rgb"/>
                </Property>
                <Property name="text" type="java.lang.String" value="Lapor Tindak Lanjut"/>
              </Properties>
              <Events>
                <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="btnLaporEvalActionPerformed"/>
              </Events>
            </Component>"""
    pf_form = pf_form.replace('<Component class="javax.swing.JButton" name="btnRefreshEval">', sub_lapor + '\n            <Component class="javax.swing.JButton" name="btnRefreshEval">')

with open('src/main/java/view/PetugasFrame.form', 'w', encoding='utf-8') as f:
    f.write(pf_form)
print("PetugasFrame.form patched!")

