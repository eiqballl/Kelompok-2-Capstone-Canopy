import os
import re

print("Starting full patch for AdminFrame, DireksiFrame, and PetugasFrame...")

# --------------------------------------------------------------------------
# 1. Update AdminFrame.form and AdminFrame.java
# --------------------------------------------------------------------------
with open('src/main/java/view/AdminFrame.form', 'r', encoding='utf-8') as f:
    af_form = f.read()

# Update lblFilterBlok text in AdminFrame.form
af_form = af_form.replace(
    '<Property name="text" type="java.lang.String" value="Filter Blok:" />',
    '<Property name="text" type="java.lang.String" value="Filter Mutu Panen:" />'
)

# Update splitEval dividerLocation in AdminFrame.form (ensure it's 340)
af_form = re.sub(
    r'(<Container class="javax.swing.JSplitPane" name="splitEval">\s*<Properties>\s*<Property name="dividerLocation" type="int" value=")420(" />)',
    r'\g<1>340\g<2>',
    af_form
)

# In pnlFilterRekap Layout dim 0 (horizontal)
old_dim0 = """                            <Component id="cmbModeRekap" alignment="0" max="32767" attributes="0" />
                            <Component id="cmbFilterKomoditas" alignment="0" max="32767" attributes="0" />
                            <Component id="txtTglAwal" alignment="0" max="32767" attributes="0" />
                            <Component id="txtTglAkhir" alignment="0" max="32767" attributes="0" />
                            <Component id="btnTampilkanRekap" alignment="0" max="32767" attributes="0" />
                            <Component id="btnResetRekap" alignment="0" max="32767" attributes="0" />
                            <Group type="102" alignment="0" attributes="0">
                              <Group type="103" groupAlignment="0" attributes="0">
                                <Component id="lblModeRekap" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblFilterKomoditas" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblTglAwal" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblTglAkhir" alignment="0" min="-2" max="-2" attributes="0" />
                              </Group>
                              <EmptySpace min="0" pref="0" max="32767" attributes="0" />
                            </Group>"""

new_dim0 = """                            <Component id="cmbModeRekap" alignment="0" max="32767" attributes="0" />
                            <Component id="cmbFilterKomoditas" alignment="0" max="32767" attributes="0" />
                            <Component id="cmbFilterBlok" alignment="0" max="32767" attributes="0" />
                            <Component id="btnPilihBlok" alignment="0" max="32767" attributes="0" />
                            <Component id="txtTglAwal" alignment="0" max="32767" attributes="0" />
                            <Component id="txtTglAkhir" alignment="0" max="32767" attributes="0" />
                            <Component id="btnTampilkanRekap" alignment="0" max="32767" attributes="0" />
                            <Component id="btnRangkumanRekap" alignment="0" max="32767" attributes="0" />
                            <Component id="btnResetRekap" alignment="0" max="32767" attributes="0" />
                            <Group type="102" alignment="0" attributes="0">
                              <Group type="103" groupAlignment="0" attributes="0">
                                <Component id="lblModeRekap" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblFilterKomoditas" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblFilterBlok" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblTglAwal" alignment="0" min="-2" max="-2" attributes="0" />
                                <Component id="lblTglAkhir" alignment="0" min="-2" max="-2" attributes="0" />
                              </Group>
                              <EmptySpace min="0" pref="0" max="32767" attributes="0" />
                            </Group>"""

if old_dim0 in af_form:
    af_form = af_form.replace(old_dim0, new_dim0)

old_dim1 = """                          <Component id="lblModeRekap" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="cmbModeRekap" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace type="unrelated" max="-2" attributes="0" />
                          <Component id="lblFilterKomoditas" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="cmbFilterKomoditas" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace type="unrelated" max="-2" attributes="0" />
                          <Component id="lblTglAwal" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="txtTglAwal" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace type="unrelated" max="-2" attributes="0" />
                          <Component id="lblTglAkhir" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="txtTglAkhir" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace min="-2" pref="24" max="-2" attributes="0" />
                          <Component id="btnTampilkanRekap" min="-2" pref="36" max="-2" attributes="0" />
                          <EmptySpace type="unrelated" max="-2" attributes="0" />
                          <Component id="btnResetRekap" min="-2" pref="36" max="-2" attributes="0" />"""

new_dim1 = """                          <Component id="lblModeRekap" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="cmbModeRekap" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="lblFilterKomoditas" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="cmbFilterKomoditas" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="lblFilterBlok" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="cmbFilterBlok" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="btnPilihBlok" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="lblTglAwal" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="txtTglAwal" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="lblTglAkhir" min="-2" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="txtTglAkhir" min="-2" pref="30" max="-2" attributes="0" />
                          <EmptySpace type="unrelated" max="-2" attributes="0" />
                          <Component id="btnTampilkanRekap" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="btnRangkumanRekap" min="-2" pref="32" max="-2" attributes="0" />
                          <EmptySpace max="-2" attributes="0" />
                          <Component id="btnResetRekap" min="-2" pref="32" max="-2" attributes="0" />"""

if old_dim1 in af_form:
    af_form = af_form.replace(old_dim1, new_dim1)

# Add btnPilihBlok and btnRangkumanRekap subcomponents in AdminFrame.form if not present
if 'name="btnPilihBlok"' not in af_form:
    sub_btns = """                    <Component class="javax.swing.JButton" name="btnPilihBlok">
                      <Properties>
                        <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                          <Font name="Segoe UI" size="12" style="0" />
                        </Property>
                        <Property name="text" type="java.lang.String" value="Pilih Blok Lahan (Semua)" />
                      </Properties>
                      <Events>
                        <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="btnPilihBlokActionPerformed" />
                      </Events>
                    </Component>
                    <Component class="javax.swing.JButton" name="btnRangkumanRekap">
                      <Properties>
                        <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                          <Color blue="4a" green="a3" red="16" type="rgb" />
                        </Property>
                        <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                          <Font name="Segoe UI" size="12" style="1" />
                        </Property>
                        <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                          <Color blue="ff" green="ff" red="ff" type="rgb" />
                        </Property>
                        <Property name="text" type="java.lang.String" value="Lihat Rangkuman Eksekutif" />
                      </Properties>
                      <Events>
                        <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="btnRangkumanRekapActionPerformed" />
                      </Events>
                    </Component>"""
    af_form = af_form.replace('<Component class="javax.swing.JButton" name="btnTampilkanRekap">', sub_btns + '\n                    <Component class="javax.swing.JButton" name="btnTampilkanRekap">')

with open('src/main/java/view/AdminFrame.form', 'w', encoding='utf-8') as f:
    f.write(af_form)

print("AdminFrame.form patched successfully!")

